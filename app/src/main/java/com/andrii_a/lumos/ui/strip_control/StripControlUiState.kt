package com.andrii_a.lumos.ui.strip_control

import com.andrii_a.lumos.domain.models.BluetoothMessage
import com.andrii_a.lumos.ui.strip_control.effects.Effect

data class StripControlUiState(
    val isBluetoothEnabled: Boolean = false,
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val errorMessage: String? = null,
    val isEffectsMenuVisible: Boolean = true,
    val selectedEffect: Effect = Effect.None,
    val lastSentInstruction: String? = null,
    val receivedMessages: List<BluetoothMessage> = emptyList()
)