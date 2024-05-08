package com.andrii_a.lumos.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.integerArrayResource
import com.andrii_a.lumos.R
import kotlin.math.sqrt
import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color as ComposeColor

val ColorStateSaver: Saver<Color, Any>
    get() {
        val redKey = "red_key"
        val greenKey = "green_key"
        val blueKey = "blue_key"

        return mapSaver(
            save = { mapOf(redKey to it.red, greenKey to it.green, blueKey to it.blue) },
            restore = {
                ComposeColor(
                    red = it[redKey] as Float,
                    green = it[greenKey] as Float,
                    blue = it[blueKey] as Float
                )
            }
        )
    }

fun isBrightColor(color: Int): Boolean {
    if (android.R.color.transparent == color) return true
    val rgb =
        intArrayOf(AndroidColor.red(color), AndroidColor.green(color), AndroidColor.blue(color))
    val brightness = sqrt(
        rgb[0] * rgb[0] * .241 + (rgb[1] * rgb[1] * .691) + rgb[2] * rgb[2] * .068
    ).toInt()
    return brightness >= 100
}

val ComposeColor.contentColor: ComposeColor
    @Composable
    get() {
        return if (isBrightColor(this.toArgb())) ComposeColor.Black else ComposeColor.White
    }

val presetColors: List<ComposeColor>
    @Composable
    get() = integerArrayResource(id = R.array.control_panel_preset_colors).map {
        ComposeColor(it)
    }

val ComposeColor.asHsvTriple: Triple<Float, Float, Float>
    get() {
        val hsv = FloatArray(3)
        AndroidColor.colorToHSV(this.toArgb(), hsv)

        return Triple(hsv[0], hsv[1], hsv[2])
    }

val ComposeColor.asHexString: String
    //@OptIn(ExperimentalStdlibApi::class)
    get() = String.format("#%06X", (0xFFFFFF and this.toArgb()))/*this.toArgb().toHexString(
        HexFormat {
            number {
                upperCase = true
                prefix = "#"
            }
        }
    )*/