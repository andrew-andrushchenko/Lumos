package com.andrii_a.lumos.ui.strip_control.components

import android.graphics.Bitmap
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toRect
import com.andrii_a.lumos.ui.util.asHsvTriple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import android.graphics.Color as AndroidColor

@Composable
fun ColorPicker(
    color: Color,
    onColorChanged: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var colorHue by remember { mutableFloatStateOf(color.asHsvTriple.first) }

    var colorSaturation by remember { mutableFloatStateOf(color.asHsvTriple.second) }

    var colorLightness by remember { mutableFloatStateOf(color.asHsvTriple.third) }

    LaunchedEffect(key1 = color) {
        val hsv = color.asHsvTriple

        colorHue = hsv.first
        colorSaturation = hsv.second
        colorLightness = hsv.third
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        SaturationLightnessPanel(
            hue = colorHue,
            saturation = colorSaturation,
            lightness = colorLightness,
            onSaturationAndValueChanged = { saturation, lightness ->
                colorSaturation = saturation
                colorLightness = lightness

                onColorChanged(Color.hsv(colorHue, colorSaturation, colorLightness))
            }
        )

        HueBar(
            hue = colorHue,
            onHueChanged = { hue ->
                colorHue = hue
                onColorChanged(Color.hsv(colorHue, colorSaturation, colorLightness))
            }
        )
    }
}

@Composable
fun HueBar(
    hue: Float,
    onHueChanged: (Float) -> Unit
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    var pressOffset by remember { mutableStateOf(Offset.Zero) }

    var selectedHue by remember { mutableFloatStateOf(hue) }
    var huePanelWidth by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(key1 = hue) {
        pressOffset = Offset(x = (hue * huePanelWidth) / 360f, y = 0f)
    }

    LaunchedEffect(key1 = pressOffset) {
        fun pointToHue(pointX: Float): Float {
            val x = when {
                pointX < 0f -> 0f
                pointX > huePanelWidth -> huePanelWidth
                else -> pointX
            }

            return x * 360f / huePanelWidth
        }

        val newHue = pointToHue(pressOffset.x)

        if (newHue.roundToInt() != selectedHue.roundToInt()) {
            selectedHue = newHue
        }
    }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { selectedHue }
            .collectLatest(onHueChanged)
    }

    Canvas(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .emitDragGesture(interactionSource)
    ) {
        val drawScopeSize = size

        val bitmap =
            Bitmap.createBitmap(size.width.toInt(), size.height.toInt(), Bitmap.Config.ARGB_8888)
        val hueCanvas = android.graphics.Canvas(bitmap)

        val huePanel = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        huePanelWidth = huePanel.width()

        val hueColors = IntArray((huePanel.width()).toInt())

        var hueIterator = 0f
        for (i in hueColors.indices) {
            hueColors[i] = AndroidColor.HSVToColor(floatArrayOf(hueIterator, 1f, 1f))
            hueIterator += 360f / hueColors.size
        }

        val linePaint = Paint()
        linePaint.strokeWidth = 0F

        for (i in hueColors.indices) {
            linePaint.color = hueColors[i]
            hueCanvas.drawLine(i.toFloat(), 0F, i.toFloat(), huePanel.bottom, linePaint)
        }

        drawBitmap(
            bitmap = bitmap,
            panel = huePanel
        )

        scope.collectForPress(interactionSource) { pressPosition ->
            val pressPos = pressPosition.x.coerceIn(0f..drawScopeSize.width)
            pressOffset = Offset(pressPos, 0f)
        }

        drawCircle(
            color = Color.White.copy(alpha = 0.9f),
            radius = size.height / 3,
            center = Offset(pressOffset.x, size.height / 2)
        )
    }
}

@Composable
fun SaturationLightnessPanel(
    hue: Float,
    saturation: Float,
    lightness: Float,
    onSaturationAndValueChanged: (Float, Float) -> Unit
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    var pressOffset by remember { mutableStateOf(Offset.Zero) }

    var saturationLightnessPair by remember { mutableStateOf(Pair(saturation, lightness)) }
    var satValPanelWidth by remember { mutableFloatStateOf(0f) }
    var satValPanelHeight by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(saturation, lightness) {
        pressOffset = Offset(
            x = saturation * satValPanelWidth,
            y = satValPanelHeight * (1 - lightness)
        )
    }

    LaunchedEffect(key1 = pressOffset) {
        fun pointToSatVal(pointX: Float, pointY: Float): Pair<Float, Float> {
            val x = when {
                pointX < 0f -> 0f
                pointX > satValPanelWidth -> satValPanelWidth
                else -> pointX
            }
            val y = when {
                pointY < 0f -> 0f
                pointY > satValPanelHeight -> satValPanelHeight
                else -> pointY
            }

            val satPoint = 1f / satValPanelWidth * x
            val valuePoint = 1f - 1f / satValPanelHeight * y

            return Pair(satPoint, valuePoint)
        }

        val newPair = pointToSatVal(pressOffset.x, pressOffset.y)

        if (newPair != saturationLightnessPair) {
            saturationLightnessPair = newPair
        }
    }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { saturationLightnessPair }
            .distinctUntilChanged()
            .collectLatest {
                onSaturationAndValueChanged(it.first, it.second)
            }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(12.dp))
            .emitDragGesture(interactionSource)
    ) {
        val cornerRadius = 12.dp.toPx()
        val saturationValuePanelSize = size
        val bitmap =
            Bitmap.createBitmap(size.width.toInt(), size.height.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = android.graphics.Canvas(bitmap)
        val saturationValuePanel = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        satValPanelWidth = saturationValuePanel.width()
        satValPanelHeight = saturationValuePanel.height()

        val rgb = AndroidColor.HSVToColor(floatArrayOf(hue, 1f, 1f))

        val satShader = LinearGradient(
            saturationValuePanel.left,
            saturationValuePanel.top,
            saturationValuePanel.right,
            saturationValuePanel.top,
            -0x1,
            rgb,
            Shader.TileMode.CLAMP
        )

        val valShader = LinearGradient(
            saturationValuePanel.left,
            saturationValuePanel.top,
            saturationValuePanel.left,
            saturationValuePanel.bottom,
            -0x1,
            -0x1000000,
            Shader.TileMode.CLAMP
        )

        canvas.drawRoundRect(
            saturationValuePanel,
            cornerRadius,
            cornerRadius,
            Paint().apply {
                shader = ComposeShader(
                    valShader,
                    satShader,
                    PorterDuff.Mode.MULTIPLY
                )
            }
        )

        drawBitmap(
            bitmap = bitmap,
            panel = saturationValuePanel
        )

        scope.collectForPress(interactionSource) { pressPosition ->
            val pressPositionOffset = Offset(
                pressPosition.x.coerceIn(0f..saturationValuePanelSize.width),
                pressPosition.y.coerceIn(0f..saturationValuePanelSize.height)
            )

            pressOffset = pressPositionOffset
        }

        drawCircle(
            color = Color.White.copy(alpha = 0.9f),
            radius = 8.dp.toPx(),
            center = pressOffset
        )
    }
}

private fun CoroutineScope.collectForPress(
    interactionSource: InteractionSource,
    setOffset: (Offset) -> Unit
) {
    launch {
        interactionSource.interactions.distinctUntilChanged().collectLatest { interaction ->
            (interaction as? PressInteraction.Press)
                ?.pressPosition
                ?.let(setOffset)
        }
    }
}

private fun Modifier.emitDragGesture(
    interactionSource: MutableInteractionSource
): Modifier = this.then(
    composed {
        val scope = rememberCoroutineScope()
        pointerInput(Unit) {
            detectDragGestures { input, _ ->
                scope.launch {
                    interactionSource.emit(PressInteraction.Press(input.position))
                }
            }
        }.clickable(interactionSource, null) {}
    }
)

private fun DrawScope.drawBitmap(
    bitmap: Bitmap,
    panel: RectF
) {
    drawIntoCanvas {
        it.nativeCanvas.drawBitmap(
            bitmap,
            null,
            panel.toRect(),
            null
        )
    }
}