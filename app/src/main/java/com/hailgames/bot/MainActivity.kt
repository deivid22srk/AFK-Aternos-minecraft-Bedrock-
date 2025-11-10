package com.hailgames.bot

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hailgames.bot.service.MinecraftBotService
import com.hailgames.bot.ui.screen.MainScreen
import com.hailgames.bot.ui.theme.HailGamesBotTheme
import com.hailgames.bot.viewmodel.BotViewModel

class MainActivity : ComponentActivity() {
    
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Handle permission result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        setContent {
            HailGamesBotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: BotViewModel = viewModel()
                    
                    MainScreen(
                        viewModel = viewModel,
                        onStartBot = { config ->
                            startBotService(config)
                        },
                        onStopBot = {
                            stopBotService()
                        }
                    )
                }
            }
        }
    }
    
    private fun startBotService(config: BotConfig) {
        val intent = Intent(this, MinecraftBotService::class.java).apply {
            putExtra("SERVER_IP", config.serverIp)
            putExtra("SERVER_PORT", config.serverPort)
            putExtra("BOT_NAME", config.botName)
            putExtra("ANTI_AFK", config.antiAfk)
            putExtra("AUTO_SNEAK", config.autoSneak)
            putExtra("CHAT_ENABLED", config.chatEnabled)
            putExtra("CHAT_MESSAGES", config.chatMessages.toTypedArray())
            putExtra("CHAT_DELAY", config.chatDelay)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
    
    private fun stopBotService() {
        val intent = Intent(this, MinecraftBotService::class.java)
        stopService(intent)
    }
}

data class BotConfig(
    val serverIp: String = "FizAnal.aternos.me",
    val serverPort: Int = 19132,
    val botName: String = "HailGamesBot",
    val antiAfk: Boolean = true,
    val autoSneak: Boolean = true,
    val chatEnabled: Boolean = true,
    val chatMessages: List<String> = listOf(
        "HailGames Afk Bot",
        "Keeping server active!",
        "Bedrock Edition Bot"
    ),
    val chatDelay: Int = 60
)
