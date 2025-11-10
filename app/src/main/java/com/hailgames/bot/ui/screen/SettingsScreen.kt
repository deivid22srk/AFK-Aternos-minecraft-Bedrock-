package com.hailgames.bot.ui.screen

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    config: BotConfig,
    onConfigChange: (BotConfig) -> Unit
) {
    var serverIp by remember { mutableStateOf(config.serverIp) }
    var serverPort by remember { mutableStateOf(config.serverPort.toString()) }
    var botName by remember { mutableStateOf(config.botName) }
    var antiAfk by remember { mutableStateOf(config.antiAfk) }
    var autoSneak by remember { mutableStateOf(config.autoSneak) }
    var chatEnabled by remember { mutableStateOf(config.chatEnabled) }
    var chatMessages by remember { mutableStateOf(config.chatMessages.joinToString("\n")) }
    var chatDelay by remember { mutableStateOf(config.chatDelay.toString()) }
    
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Configurações",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Server Settings Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Servidor",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                OutlinedTextField(
                    value = serverIp,
                    onValueChange = { serverIp = it },
                    label = { Text("Endereço do Servidor") },
                    placeholder = { Text("FizAnal.aternos.me") },
                    leadingIcon = { Icon(Icons.Default.Dns, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = serverPort,
                    onValueChange = { serverPort = it.filter { char -> char.isDigit() } },
                    label = { Text("Porta") },
                    placeholder = { Text("19132") },
                    leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = botName,
                    onValueChange = { botName = it },
                    label = { Text("Nome do Bot") },
                    placeholder = { Text("HailGamesBot") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
        
        // Anti-AFK Settings Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Anti-AFK",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DirectionsRun,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                text = "Ativar Anti-AFK",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Pula automaticamente",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = antiAfk,
                        onCheckedChange = { antiAfk = it }
                    )
                }
                
                if (antiAfk) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = "Auto Agachar",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Agacha enquanto pula",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = autoSneak,
                            onCheckedChange = { autoSneak = it }
                        )
                    }
                }
            }
        }
        
        // Chat Settings Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Chat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Mensagens Automáticas",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Switch(
                        checked = chatEnabled,
                        onCheckedChange = { chatEnabled = it }
                    )
                }
                
                if (chatEnabled) {
                    OutlinedTextField(
                        value = chatMessages,
                        onValueChange = { chatMessages = it },
                        label = { Text("Mensagens (uma por linha)") },
                        placeholder = { Text("HailGames Afk Bot\nKeeping server active!") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 10
                    )
                    
                    OutlinedTextField(
                        value = chatDelay,
                        onValueChange = { chatDelay = it.filter { char -> char.isDigit() } },
                        label = { Text("Delay entre mensagens (segundos)") },
                        placeholder = { Text("60") },
                        leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
        }
        
        // Save Button
        FilledTonalButton(
            onClick = {
                val messages = if (chatMessages.isNotBlank()) {
                    chatMessages.split("\n").filter { it.isNotBlank() }
                } else {
                    config.chatMessages
                }
                
                onConfigChange(
                    config.copy(
                        serverIp = serverIp.ifBlank { config.serverIp },
                        serverPort = serverPort.toIntOrNull() ?: config.serverPort,
                        botName = botName.ifBlank { config.botName },
                        antiAfk = antiAfk,
                        autoSneak = autoSneak,
                        chatEnabled = chatEnabled,
                        chatMessages = messages,
                        chatDelay = chatDelay.toIntOrNull() ?: config.chatDelay
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("SALVAR CONFIGURAÇÕES", style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
