package com.andrii_a.lumos.ui.strip_control.effects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrii_a.lumos.R
import com.andrii_a.lumos.ui.strip_control.components.BrightnessControlSlider
import com.andrii_a.lumos.ui.strip_control.components.SliderWithButtons
import com.andrii_a.lumos.ui.theme.LumosTheme

@Composable
fun FirefliesEffectControls(
    onBrightnessChanged: (Float) -> Unit,
    onFirefliesAmountChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Icon(
                painter = painterResource(id = R.drawable.ic_fireflies),
                contentDescription = stringResource(id = R.string.effect_fireflies),
                tint = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .size(128.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            BrightnessControlSlider(
                onBrightnessSet = { brightness ->
                    onBrightnessChanged(brightness)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            var sliderPosition by remember { mutableFloatStateOf(3f) }

            LaunchedEffect(key1 = sliderPosition) {
                onFirefliesAmountChanged(sliderPosition.toInt())
            }

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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(
                    id = R.string.fireflies_amount_formatted,
                    sliderPosition.toInt()
                ),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun FirefliesEffectControlsPreview() {
    LumosTheme {
        Surface {
            FirefliesEffectControls(
                onBrightnessChanged = {},
                onFirefliesAmountChanged = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}