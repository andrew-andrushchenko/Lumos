package com.andrii_a.lumos.domain

object CommProtocol {
    fun changeEffect(effectId: Int) = "<e$effectId>"

    fun changeBrightness(brightnessLevel: Int) = "<b$brightnessLevel>"

    fun changeFireplaceHue(hue: Float) = "<h$hue>"

    //TODO: To be complemented later.
}