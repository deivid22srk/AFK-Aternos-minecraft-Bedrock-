package com.hailgames.bot.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.hailgames.bot.MainActivity
import com.hailgames.bot.R
import kotlinx.coroutines.*
import org.cloudburstmc.protocol.bedrock.BedrockClient
import org.cloudburstmc.protocol.bedrock.BedrockClientSession
import org.cloudburstmc.protocol.bedrock.packet.TextPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType
import java.net.InetSocketAddress

class MinecraftBotService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var wakeLock: PowerManager.WakeLock? = null
    
    private var bedrockClient: BedrockClient? = null
    private var session: BedrockClientSession? = null
    
    private var antiAfkJob: Job? = null
    private var chatJob: Job? = null
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "hailgames_bot_channel"
        private const val CHANNEL_NAME = "HailGames Bot Service"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        // Acquire wake lock to keep CPU running
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
            val serverPort = it.getIntExtra("SERVER_PORT", 19132)
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
        try {
            updateNotification("Conectando a $serverIp...", false)
            
            // Create Bedrock client
            bedrockClient = BedrockClient(InetSocketAddress("0.0.0.0", 0))
            bedrockClient?.bind()?.join()
            
            // Connect to server
            val address = InetSocketAddress(serverIp, serverPort)
            session = bedrockClient?.connect(address)?.join()
            
            updateNotification("Conectado a $serverIp", true)
            
            // Start Anti-AFK
            if (antiAfk) {
                startAntiAFK(autoSneak)
            }
            
            // Start Chat Messages
            if (chatEnabled && chatMessages.isNotEmpty()) {
                startChatMessages(chatMessages, chatDelay)
            }
            
        } catch (e: Exception) {
            updateNotification("Erro: ${e.message}", false)
            delay(5000)
            // Auto-reconnect
            connectToServer(
                serverIp, serverPort, botName,
                antiAfk, autoSneak,
                chatEnabled, chatMessages, chatDelay
            )
        }
    }
    
    private fun startAntiAFK(autoSneak: Boolean) {
        antiAfkJob?.cancel()
        antiAfkJob = serviceScope.launch {
            var sneaking = false
            while (isActive) {
                try {
                    // Jump action
                    val jumpPacket = PlayerActionPacket()
                    jumpPacket.action = PlayerActionType.JUMP
                    session?.sendPacket(jumpPacket)
                    
                    // Sneak action (toggle)
                    if (autoSneak) {
                        val sneakPacket = PlayerActionPacket()
                        sneakPacket.action = if (sneaking) {
                            PlayerActionType.STOP_SNEAK
                        } else {
                            PlayerActionType.START_SNEAK
                        }
                        session?.sendPacket(sneakPacket)
                        sneaking = !sneaking
                    }
                    
                    delay(3000) // Every 3 seconds
                } catch (e: Exception) {
                    // Continue loop
                }
            }
        }
    }
    
    private fun startChatMessages(messages: List<String>, delaySeconds: Int) {
        chatJob?.cancel()
        chatJob = serviceScope.launch {
            var index = 0
            while (isActive) {
                try {
                    val textPacket = TextPacket()
                    textPacket.type = TextPacket.Type.CHAT
                    textPacket.message = messages[index]
                    textPacket.sourceName = ""
                    textPacket.xuid = ""
                    session?.sendPacket(textPacket)
                    
                    index = (index + 1) % messages.size
                    delay(delaySeconds * 1000L)
                } catch (e: Exception) {
                    // Continue loop
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
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("HailGames Bot")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
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
        
        // Cancel all jobs
        antiAfkJob?.cancel()
        chatJob?.cancel()
        
        // Close connection
        session?.disconnect()
        bedrockClient?.close()
        
        // Cancel service scope
        serviceScope.cancel()
        
        // Release wake lock
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
