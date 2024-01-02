package com.andrii_a.lumos.ui.strip_control.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.andrii_a.lumos.ui.theme.LumosTheme

@Composable
fun SliderWithButtons(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    steps: Int = 0,
    onPlusButtonClick: () -> Unit,
    onMinusButtonClick: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (minusButton, plusButton, slider) = createRefs()

        FilledTonalIconButton(
            onClick = onMinusButtonClick,
            modifier = Modifier.constrainAs(minusButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
        ) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = null)
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.constrainAs(slider) {
                top.linkTo(minusButton.top)
                bottom.linkTo(minusButton.bottom)
                start.linkTo(minusButton.end, 8.dp)
                end.linkTo(plusButton.start, 8.dp)

                width = Dimension.fillToConstraints
            }
        )

        FilledTonalIconButton(
            onClick = onPlusButtonClick,
            modifier = Modifier.constrainAs(plusButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun SliderWithButtonsPreview() {
    LumosTheme {
        Surface {
            var sliderPosition by remember { mutableFloatStateOf(3f) }

            SliderWithButtons(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = 1f..10f,
                steps = 8,
                onPlusButtonClick = {
                    if (sliderPosition < 10f) {
                        sliderPosition++
                    }
                },
                onMinusButtonClick = {
                    if (sliderPosition > 1f) {
                        sliderPosition--
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}