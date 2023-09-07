package com.andrii_a.lumos.ui.stripe_control

data class StripeControlUiState(
    val isBluetoothEnabled: Boolean = false,
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val errorMessage: String? = null,
)