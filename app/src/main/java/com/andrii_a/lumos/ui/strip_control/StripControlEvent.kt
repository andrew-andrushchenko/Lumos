package com.andrii_a.lumos.ui.strip_control

import com.andrii_a.lumos.ui.strip_control.effects.Effect

sealed interface StripControlEvent {
    data class ConnectToDevice(val address: String) : StripControlEvent
    data object DisconnectFromDevice : StripControlEvent
    data object ShowEffectsMenu : StripControlEvent
    data class ChangeEffect(val effect: Effect) : StripControlEvent
    data class ChangeFireplaceColor(val colorHexString: String) : StripControlEvent
    data class ChangeBrightness(val brightness: Float) : StripControlEvent
    data class ChangeFirefliesAmount(val amount: Int) : StripControlEvent
}