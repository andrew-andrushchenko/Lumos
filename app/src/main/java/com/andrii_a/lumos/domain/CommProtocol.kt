package com.andrii_a.lumos.domain

object CommProtocol {
    fun changeEffect(effectId: Int) = "<e$effectId>"

    fun changeBrightness(brightnessLevel: Int) = "<b$brightnessLevel>"

    fun changeFireplaceHue(hue: Float) = "<h$hue>"

    fun changeFirefliesAmount(amount: Int) = "<fa$amount>"

    //TODO: To be complemented later.
}