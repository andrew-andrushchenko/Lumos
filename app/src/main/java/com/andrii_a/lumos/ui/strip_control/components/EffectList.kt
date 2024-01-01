package com.andrii_a.lumos.ui.strip_control.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrii_a.lumos.ui.strip_control.effects.Effect
import com.andrii_a.lumos.ui.theme.LumosTheme

@Composable
fun EffectList(
    onEffectSelected: (Effect) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Effect.entries.forEach { effect ->
            EffectItem(
                effect = effect,
                onClick = { onEffectSelected(effect) }
            )
        }
    }
}

@Composable
fun EffectItem(
    effect: Effect,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListItem(
            leadingContent = {
                Icon(
                    painter = painterResource(id = effect.iconRes),
                    contentDescription = effect.name,
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        )
                        .padding(12.dp)
                )
            },
            headlineContent = {
                Text(text = stringResource(id = effect.nameRes))
            },
            modifier = Modifier.clickable(onClick = onClick)
        )

        Divider()
    }
}

@Preview
@Composable
fun EffectListPreview() {
    LumosTheme {
        Surface {
            EffectList(onEffectSelected = {})
        }
    }
}

@Preview
@Composable
fun EffectItemPreview() {
    LumosTheme {
        Surface {
            EffectItem(effect = Effect.Fireflies, onClick = {})
        }
    }
}