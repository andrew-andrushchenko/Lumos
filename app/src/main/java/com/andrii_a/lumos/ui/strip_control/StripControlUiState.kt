package com.andrii_a.lumos.ui.strip_control

data class StripControlUiState(
    val isBluetoothEnabled: Boolean = false,
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val errorMessage: String? = null,
)