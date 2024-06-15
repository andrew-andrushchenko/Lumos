package com.andrii_a.lumos.ui.strip_control.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.andrii_a.lumos.R
import com.andrii_a.lumos.ui.theme.LumosTheme

@Composable
fun BrightnessControlSlider(
    onBrightnessSet: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by rememberSaveable { mutableFloatStateOf(0f) }

    ConstraintLayout(modifier = modifier) {
        val (brightnessIcon, slider, brightnessValueText) = createRefs()

        Icon(
            imageVector = Icons.Outlined.Brightness5,
            contentDescription = null,
            modifier = Modifier
                .constrainAs(brightnessIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .padding(8.dp)
        )

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                onBrightnessSet(sliderPosition)
            },
            modifier = Modifier.constrainAs(slider) {
                top.linkTo(brightnessIcon.top)
                bottom.linkTo(brightnessIcon.bottom)
                start.linkTo(brightnessIcon.end, 16.dp)
                end.linkTo(brightnessValueText.start, 16.dp)

                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = stringResource(
                id = R.string.brightness_level_formatted,
                sliderPosition.toInt()
            ),
            modifier = Modifier.constrainAs(brightnessValueText) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        )

    }
}

@Preview
@Composable
fun BrightnessControlSliderPreview() {
    LumosTheme {
        Surface {
            BrightnessControlSlider(
                onBrightnessSet = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}