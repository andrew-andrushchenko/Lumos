package com.andrii_a.lumos.ui.strip_control.effects

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.andrii_a.lumos.R

enum class Effect(
    val id: Int,
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int
) {
    None(id = 0, nameRes = R.string.effect_off, iconRes = R.drawable.ic_led_off),
    Fireplace(id = 1, nameRes = R.string.effect_fireplace, iconRes = R.drawable.ic_fireplace),
    LavaLamp(id = 2, nameRes = R.string.effect_lava_lamp, iconRes = R.drawable.ic_lava_lamp),
    Rainbow(id = 3, nameRes = R.string.effect_rainbow, iconRes = R.drawable.ic_rainbow),
    TheaterRainbow(id = 4, nameRes = R.string.effect_theater_rainbow, iconRes = R.drawable.ic_rainbow),
    Plasma(id = 5, nameRes = R.string.effect_plasma, iconRes = R.drawable.ic_plasma),
    Fireflies(id = 6, nameRes = R.string.effect_fireflies, iconRes = R.drawable.ic_fireflies),
    Sparkles(id = 7, nameRes = R.string.effect_sparkles, iconRes = R.drawable.ic_sparkles)
}