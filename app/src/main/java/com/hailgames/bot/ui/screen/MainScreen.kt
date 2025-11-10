package com.hailgames.bot.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hailgames.bot.BotConfig
import com.hailgames.bot.viewmodel.BotViewModel
import com.hailgames.bot.viewmodel.BotState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BotViewModel,
    onStartBot: (BotConfig) -> Unit,
    onStopBot: () -> Unit
) {
    val botState by viewModel.botState.collectAsState()
    val config by viewModel.config.collectAsState()
    
    var showSettings by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HailGames Bot") },
                actions = {
                    IconButton(onClick = { showSettings = !showSettings }) {
                        Icon(
                            imageVector = if (showSettings) Icons.Default.Close else Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = showSettings,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            transitionSpec = {
                fadeIn() + slideInHorizontally() togetherWith
                        fadeOut() + slideOutHorizontally()
            }
        ) { showSettingsScreen ->
            if (showSettingsScreen) {
                SettingsScreen(
                    config = config,
                    onConfigChange = { viewModel.updateConfig(it) }
                )
            } else {
                BotStatusScreen(
                    botState = botState,
                    config = config,
                    onStartBot = {
                        viewModel.setBotState(BotState.CONNECTING)
                        onStartBot(config)
                    },
                    onStopBot = {
                        viewModel.setBotState(BotState.DISCONNECTED)
                        onStopBot()
                    }
                )
            }
        }
    }
}

@Composable
fun BotStatusScreen(
    botState: BotState,
    config: BotConfig,
    onStartBot: () -> Unit,
    onStopBot: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (botState) {
                    BotState.DISCONNECTED -> MaterialTheme.colorScheme.surfaceVariant
                    BotState.CONNECTING -> MaterialTheme.colorScheme.tertiaryContainer
                    BotState.CONNECTED -> MaterialTheme.colorScheme.primaryContainer
                    BotState.ERROR -> MaterialTheme.colorScheme.errorContainer
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = when (botState) {
                        BotState.DISCONNECTED -> Icons.Default.CloudOff
                        BotState.CONNECTING -> Icons.Default.CloudSync
                        BotState.CONNECTED -> Icons.Default.CloudDone
                        BotState.ERROR -> Icons.Default.ErrorOutline
                    },
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = when (botState) {
                        BotState.DISCONNECTED -> MaterialTheme.colorScheme.onSurfaceVariant
                        BotState.CONNECTING -> MaterialTheme.colorScheme.onTertiaryContainer
                        BotState.CONNECTED -> MaterialTheme.colorScheme.onPrimaryContainer
                        BotState.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                    }
                )
                
                Text(
                    text = when (botState) {
                        BotState.DISCONNECTED -> "Desconectado"
                        BotState.CONNECTING -> "Conectando..."
                        BotState.CONNECTED -> "Conectado!"
                        BotState.ERROR -> "Erro de conexão"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = when (botState) {
                        BotState.DISCONNECTED -> MaterialTheme.colorScheme.onSurfaceVariant
                        BotState.CONNECTING -> MaterialTheme.colorScheme.onTertiaryContainer
                        BotState.CONNECTED -> MaterialTheme.colorScheme.onPrimaryContainer
                        BotState.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                    }
                )
                
                if (botState == BotState.CONNECTING) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        // Server Info
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Configurações Atuais",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                InfoRow(icon = Icons.Default.Dns, label = "Servidor", value = config.serverIp)
                InfoRow(icon = Icons.Default.Numbers, label = "Porta", value = config.serverPort.toString())
                InfoRow(icon = Icons.Default.Person, label = "Nome", value = config.botName)
                InfoRow(
                    icon = Icons.Default.DirectionsRun,
                    label = "Anti-AFK",
                    value = if (config.antiAfk) "Ativado" else "Desativado"
                )
                InfoRow(
                    icon = Icons.Default.Chat,
                    label = "Mensagens",
                    value = if (config.chatEnabled) "Ativadas (${config.chatMessages.size})" else "Desativadas"
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Control Button
        FilledTonalButton(
            onClick = {
                if (botState == BotState.CONNECTED || botState == BotState.CONNECTING) {
                    onStopBot()
                } else {
                    onStartBot()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (botState == BotState.CONNECTED || botState == BotState.CONNECTING)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Icon(
                imageVector = if (botState == BotState.CONNECTED || botState == BotState.CONNECTING)
                    Icons.Default.Stop
                else
                    Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (botState == BotState.CONNECTED || botState == BotState.CONNECTING)
                    "PARAR BOT"
                else
                    "INICIAR BOT",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
