package com.andrii_a.lumos.ui.strip_control

sealed interface StripControlEvent {
    data class ConnectToDevice(val address: String) : StripControlEvent
    data object DisconnectFromDevice : StripControlEvent
    data class ChangeColor(val colorHSV: Triple<Float, Float, Float>) : StripControlEvent
    data class ChangeBrightness(val brightness: Float) : StripControlEvent
}