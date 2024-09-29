package com.andrii_a.lumos.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Devices : Screen

    @Serializable
    data class StripControl(val btDeviceAddress: String) : Screen
}