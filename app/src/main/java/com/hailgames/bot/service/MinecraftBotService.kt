package com.hailgames.bot.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.hailgames.bot.MainActivity
import kotlinx.coroutines.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MinecraftBotService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var wakeLock: PowerManager.WakeLock? = null
    
    private var socket: DatagramSocket? = null
    private var isConnected = false
    
    private var antiAfkJob: Job? = null
    private var chatJob: Job? = null
    private var keepAliveJob: Job? = null
    
    private var serverAddress: InetAddress? = null
    private var serverPort: Int = 19132
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "hailgames_bot_channel"
        private const val CHANNEL_NAME = "HailGames Bot Service"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "HailGamesBot::BotWakeLock"
        ).apply {
            acquire()
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("Iniciando...", false)
        startForeground(NOTIFICATION_ID, notification)
        
        intent?.let {
            val serverIp = it.getStringExtra("SERVER_IP") ?: "FizAnal.aternos.me"
            serverPort = it.getIntExtra("SERVER_PORT", 19132)
            val botName = it.getStringExtra("BOT_NAME") ?: "HailGamesBot"
            val antiAfk = it.getBooleanExtra("ANTI_AFK", true)
            val autoSneak = it.getBooleanExtra("AUTO_SNEAK", true)
            val chatEnabled = it.getBooleanExtra("CHAT_ENABLED", true)
            val chatMessages = it.getStringArrayExtra("CHAT_MESSAGES")?.toList() ?: listOf()
            val chatDelay = it.getIntExtra("CHAT_DELAY", 60)
            
            serviceScope.launch {
                connectToServer(
                    serverIp, serverPort, botName,
                    antiAfk, autoSneak,
                    chatEnabled, chatMessages, chatDelay
                )
            }
        }
        
        return START_STICKY
    }
    
    private suspend fun connectToServer(
        serverIp: String,
        serverPort: Int,
        botName: String,
        antiAfk: Boolean,
        autoSneak: Boolean,
        chatEnabled: Boolean,
        chatMessages: List<String>,
        chatDelay: Int
    ) {
        withContext(Dispatchers.IO) {
            try {
                updateNotification("Conectando a $serverIp:$serverPort...", false)
                
                serverAddress = InetAddress.getByName(serverIp)
                socket = DatagramSocket()
                
                sendUnconnectedPing()
                
                delay(2000)
                isConnected = true
                updateNotification("Conectado a $serverIp", true)
                
                if (antiAfk) {
                    startAntiAFK(autoSneak)
                }
                
                if (chatEnabled && chatMessages.isNotEmpty()) {
                    startChatMessages(chatMessages, chatDelay)
                }
                
                startKeepAlive()
                
            } catch (e: Exception) {
                updateNotification("Erro: ${e.message}", false)
                delay(5000)
                connectToServer(
                    serverIp, serverPort, botName,
                    antiAfk, autoSneak,
                    chatEnabled, chatMessages, chatDelay
                )
            }
        }
    }
    
    private fun sendUnconnectedPing() {
        try {
            val pingData = ByteArray(35)
            pingData[0] = 0x01
            
            val currentTime = System.currentTimeMillis()
            for (i in 0..7) {
                pingData[1 + i] = (currentTime shr (8 * (7 - i))).toByte()
            }
            
            val magic = byteArrayOf(
                0x00, 0xff.toByte(), 0xff.toByte(), 0x00,
                0xfe.toByte(), 0xfe.toByte(), 0xfe.toByte(), 0xfe.toByte(),
                0xfd.toByte(), 0xfd.toByte(), 0xfd.toByte(), 0xfd.toByte(),
                0x12, 0x34, 0x56, 0x78
            )
            System.arraycopy(magic, 0, pingData, 9, 16)
            
            val clientGuid = System.currentTimeMillis()
            for (i in 0..7) {
                pingData[25 + i] = (clientGuid shr (8 * (7 - i))).toByte()
            }
            
            val packet = DatagramPacket(pingData, pingData.size, serverAddress, serverPort)
            socket?.send(packet)
            
        } catch (e: Exception) {
            android.util.Log.e("HailGamesBot", "Ping error: ${e.message}")
        }
    }
    
    private fun startKeepAlive() {
        keepAliveJob?.cancel()
        keepAliveJob = serviceScope.launch {
            while (isActive && isConnected) {
                try {
                    sendUnconnectedPing()
                    delay(30000)
                } catch (e: Exception) {
                    android.util.Log.e("HailGamesBot", "Keep alive error: ${e.message}")
                }
            }
        }
    }
    
    private fun startAntiAFK(autoSneak: Boolean) {
        antiAfkJob?.cancel()
        antiAfkJob = serviceScope.launch {
            var actionCount = 0
            while (isActive && isConnected) {
                try {
                    android.util.Log.d("HailGamesBot", "Anti-AFK: Jump ${if (autoSneak) "+ Sneak" else ""}")
                    actionCount++
                    delay(3000)
                } catch (e: Exception) {
                    android.util.Log.e("HailGamesBot", "Anti-AFK error: ${e.message}")
                }
            }
        }
    }
    
    private fun startChatMessages(messages: List<String>, delaySeconds: Int) {
        chatJob?.cancel()
        chatJob = serviceScope.launch {
            var index = 0
            while (isActive && isConnected) {
                try {
                    val message = messages[index]
                    android.util.Log.d("HailGamesBot", "Chat: $message")
                    
                    index = (index + 1) % messages.size
                    delay(delaySeconds * 1000L)
                } catch (e: Exception) {
                    android.util.Log.e("HailGamesBot", "Chat error: ${e.message}")
                }
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificação do HailGames Bot"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(message: String, isConnected: Boolean): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val icon = if (isConnected) {
            android.R.drawable.presence_online
        } else {
            android.R.drawable.presence_invisible
        }
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("HailGames Bot")
            .setContentText(message)
            .setSmallIcon(icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
    
    private fun updateNotification(message: String, isConnected: Boolean) {
        val notification = createNotification(message, isConnected)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        isConnected = false
        
        antiAfkJob?.cancel()
        chatJob?.cancel()
        keepAliveJob?.cancel()
        
        socket?.close()
        serviceScope.cancel()
        
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
