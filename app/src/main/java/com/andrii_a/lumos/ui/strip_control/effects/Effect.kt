package com.andrii_a.lumos.ui.strip_control.effects

import androidx.annotation.StringRes
import com.andrii_a.lumos.R

enum class Effect(
    val id: Int,
    @StringRes val nameRes: Int
) {
    StripOff(id = 0, nameRes = R.string.effect_off),
    Fireplace(id = 1, nameRes = R.string.effect_fireplace),
    LavaLamp(id = 2, nameRes = R.string.effect_lava_lamp),
    Rainbow(id = 3, nameRes = R.string.effect_rainbow),
    TheaterRainbow(id = 4, nameRes = R.string.effect_theater_rainbow),
    Plasma(id = 5, nameRes = R.string.effect_plasma),
    Fireflies(id = 6, nameRes = R.string.effect_fireflies),
    Sparkles(id = 7, nameRes = R.string.effect_sparkles)
}