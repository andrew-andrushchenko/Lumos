package com.andrii_a.lumos.ui.strip_control

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.andrii_a.lumos.R
import com.andrii_a.lumos.ui.strip_control.components.ColorPicker
import com.andrii_a.lumos.ui.theme.LumosTheme
import com.andrii_a.lumos.ui.util.ColorStateSaver
import com.andrii_a.lumos.ui.util.asHexString
import com.andrii_a.lumos.ui.util.asHsvTriple
import com.andrii_a.lumos.ui.util.contentColor
import com.andrii_a.lumos.ui.util.presetColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StripControlScreen(
    state: StripControlUiState,
    onEvent: (StripControlEvent) -> Unit,
) {
    BackHandler(enabled = true) {
        onEvent(StripControlEvent.DisconnectFromDevice)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when {
                        state.isConnecting -> {
                            Text(text = stringResource(id = R.string.connecting_title))
                        }

                        state.isConnected -> {
                            Text(text = stringResource(id = R.string.strip_controls_title))
                        }
                    }
                },
                navigationIcon = {
                    if (state.isConnected) {
                        IconButton(onClick = { onEvent(StripControlEvent.DisconnectFromDevice) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.disconnect_from_device)
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        when {
            state.isConnecting -> {
                ConnectingStateContent(
                    onEvent = onEvent,
                    contentPadding = innerPadding
                )
            }

            state.isConnected -> {
                ConnectedStateContent(
                    onEvent = onEvent,
                    contentPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun ConnectingStateContent(
    onEvent: (StripControlEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.loading_animation_lights_strip)
        )

        LottieAnimation(
            composition = composition,
            iterations = Int.MAX_VALUE,
            reverseOnRepeat = true,
            modifier = Modifier
                .requiredHeight(150.dp)
                .scale(1.3f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.connection_state_banner_text),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { onEvent(StripControlEvent.DisconnectFromDevice) }) {
            Text(text = stringResource(id = R.string.cancel))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConnectedStateContent(
    onEvent: (StripControlEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        var sliderPosition by rememberSaveable { mutableFloatStateOf(0f) }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.brightness),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    id = R.string.brightness_level_formatted,
                    sliderPosition.toInt()
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                onEvent(StripControlEvent.ChangeBrightness(sliderPosition))
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        val (selectedColor, onColorSelected) = rememberSaveable(stateSaver = ColorStateSaver) {
            mutableStateOf(Color(context.getColor(R.color.preset_orange)))
        }

        LaunchedEffect(key1 = selectedColor) {
            snapshotFlow { selectedColor }
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest {
                    onEvent(StripControlEvent.ChangeColor(it.asHsvTriple))
                }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.pick_a_color),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .width(110.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = selectedColor,
                            cornerRadius = CornerRadius(16.dp.toPx())
                        )
                    }
                    .padding(12.dp)
            ) {
                Text(
                    text = selectedColor.asHexString,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = selectedColor.contentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ColorPicker(
            color = selectedColor,
            onColorChanged = onColorSelected,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.preset_colors),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .selectableGroup()
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    val hsv = selectedColor.asHsvTriple

                    val generatedColor = Color.hsv((0..360).random().toFloat(), hsv.second, hsv.third)
                    onColorSelected(generatedColor)
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(84.dp)
                        .padding(12.dp)
                        .border(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                            width = 3.dp
                        )
                ) {
                    Icon(
                        Icons.Outlined.AutoAwesome,
                        contentDescription = stringResource(id = R.string.generate_random_hue),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            presetColors.forEach { color ->
                PresetColorItem(
                    color = color,
                    selected = color == selectedColor,
                    onSelected = { onColorSelected(color) }
                )
            }
        }
    }
}

@Composable
private fun PresetColorItem(
    modifier: Modifier = Modifier,
    color: Color,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onSelected,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(84.dp)
                .padding(12.dp)
                .drawBehind {
                    drawCircle(color)
                }
        ) {
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .drawBehind {
                            drawCircle(Color.White)
                        }
                        .padding(6.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

private class StripControlUiStateProvider : PreviewParameterProvider<StripControlUiState> {
    override val values = sequenceOf(
        StripControlUiState(isConnecting = true),
        StripControlUiState(isConnected = true)
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StripControlScreenPreview(@PreviewParameter(StripControlUiStateProvider::class) state: StripControlUiState) {
    LumosTheme {
        StripControlScreen(
            state = state,
            onEvent = {}
        )
    }
}