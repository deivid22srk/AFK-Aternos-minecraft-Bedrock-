package com.hailgames.bot.viewmodel

import androidx.lifecycle.ViewModel
import com.hailgames.bot.BotConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class BotState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

class BotViewModel : ViewModel() {
    private val _botState = MutableStateFlow(BotState.DISCONNECTED)
    val botState: StateFlow<BotState> = _botState.asStateFlow()
    
    private val _config = MutableStateFlow(BotConfig())
    val config: StateFlow<BotConfig> = _config.asStateFlow()
    
    fun setBotState(state: BotState) {
        _botState.value = state
    }
    
    fun updateConfig(newConfig: BotConfig) {
        _config.value = newConfig
    }
}
