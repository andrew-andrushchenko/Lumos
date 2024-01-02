package com.andrii_a.lumos.ui.strip_control.effects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrii_a.lumos.R
import com.andrii_a.lumos.ui.strip_control.components.BrightnessControlSlider
import com.andrii_a.lumos.ui.theme.LumosTheme

@Composable
fun TheaterRainbowEffectControls(
    onBrightnessChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_rainbow),
                contentDescription = stringResource(id = R.string.effect_theater_rainbow),
                tint = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .size(128.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                    .padding(24.dp)
            )

            BrightnessControlSlider(
                onBrightnessSet = { brightness ->
                    onBrightnessChanged(brightness)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun TheaterRainbowEffectControlsPreview() {
    LumosTheme {
        Surface {
            TheaterRainbowEffectControls(
                onBrightnessChanged = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}