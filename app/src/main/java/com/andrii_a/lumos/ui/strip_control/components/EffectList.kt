package com.andrii_a.lumos.ui.strip_control.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrii_a.lumos.ui.strip_control.effects.Effect
import com.andrii_a.lumos.ui.strip_control.effects.menuListItems
import com.andrii_a.lumos.ui.theme.LumosTheme

@Composable
fun EffectList(
    onEffectSelected: (Effect) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(menuListItems) { effect ->
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
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = effect.iconRes),
                contentDescription = effect.name,
                tint = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )

            Text(
                text = stringResource(id = effect.nameRes),
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        }
    }
}

@Preview
@Composable
fun EffectListPreview() {
    LumosTheme {
        Surface {
            EffectList(
                onEffectSelected = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
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