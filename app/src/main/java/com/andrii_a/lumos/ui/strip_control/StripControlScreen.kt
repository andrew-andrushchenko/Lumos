package com.andrii_a.lumos.ui.strip_control

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.andrii_a.lumos.ui.strip_control.components.EffectList
import com.andrii_a.lumos.ui.strip_control.effects.Effect
import com.andrii_a.lumos.ui.strip_control.effects.FirefliesEffectControls
import com.andrii_a.lumos.ui.strip_control.effects.FireplaceEffectControls
import com.andrii_a.lumos.ui.strip_control.effects.LavaLampEffectControls
import com.andrii_a.lumos.ui.strip_control.effects.PlasmaEffectControls
import com.andrii_a.lumos.ui.strip_control.effects.RainbowEffectControls
import com.andrii_a.lumos.ui.strip_control.effects.SparklesEffectControls
import com.andrii_a.lumos.ui.strip_control.effects.TheaterRainbowEffectControls
import com.andrii_a.lumos.ui.theme.LumosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StripControlScreen(
    state: StripControlUiState,
    onEvent: (StripControlEvent) -> Unit,
) {
    BackHandler(enabled = true) {
        if (state.isEffectsMenuVisible) {
            onEvent(StripControlEvent.DisconnectFromDevice)
        } else {
            onEvent(StripControlEvent.ShowEffectsMenu)
        }
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
                            if (state.isEffectsMenuVisible) {
                                Text(text = stringResource(id = R.string.strip_controls_menu_title))
                            } else {
                                Text(text = stringResource(id = state.selectedEffect.nameRes))
                            }
                        }
                    }
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = state.isConnected,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        AnimatedContent(
                            targetState = state.isEffectsMenuVisible,
                            label = "AnimatedTopBarNavigationButton"
                        ) { isMenuVisible ->
                            if (isMenuVisible) {
                                IconButton(onClick = { onEvent(StripControlEvent.DisconnectFromDevice) }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(id = R.string.disconnect_from_device)
                                    )
                                }
                            } else {
                                IconButton(onClick = { onEvent(StripControlEvent.ShowEffectsMenu) }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(id = R.string.disconnect_from_device)
                                    )
                                }
                            }
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
                    state = state,
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

@Composable
private fun ConnectedStateContent(
    state: StripControlUiState,
    onEvent: (StripControlEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    if (state.isEffectsMenuVisible) {
        EffectList(
            onEffectSelected = { effect ->
                onEvent(StripControlEvent.ChangeEffect(effect))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        )
    } else {
        EffectControls(
            effect = state.selectedEffect,
            onEvent = onEvent,
            contentPadding = contentPadding
        )
    }
}

@Composable
private fun EffectControls(
    effect: Effect,
    onEvent: (StripControlEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    when (effect) {
        Effect.None -> Unit

        Effect.Fireplace -> {
            FireplaceEffectControls(
                onBrightnessChanged = { brightness ->
                    onEvent(StripControlEvent.ChangeBrightness(brightness))
                },
                onHueChanged = { hue ->
                    onEvent(StripControlEvent.ChangeFireplaceHue(hue))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }

        Effect.LavaLamp -> {
            LavaLampEffectControls(
                onBrightnessChanged = { brightness ->
                    onEvent(StripControlEvent.ChangeBrightness(brightness))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }

        Effect.Rainbow -> {
            RainbowEffectControls(
                onBrightnessChanged = { brightness ->
                    onEvent(StripControlEvent.ChangeBrightness(brightness))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }

        Effect.TheaterRainbow -> {
            TheaterRainbowEffectControls(
                onBrightnessChanged = { brightness ->
                    onEvent(StripControlEvent.ChangeBrightness(brightness))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }

        Effect.Plasma -> {
            PlasmaEffectControls(
                onBrightnessChanged = { brightness ->
                    onEvent(StripControlEvent.ChangeBrightness(brightness))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }

        Effect.Fireflies -> {
            FirefliesEffectControls(
                onBrightnessChanged = { brightness ->
                    onEvent(StripControlEvent.ChangeBrightness(brightness))
                },
                onFirefliesAmountChanged = { amount ->
                    onEvent(StripControlEvent.ChangeFirefliesAmount(amount))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }

        Effect.Sparkles -> {
            SparklesEffectControls(
                onBrightnessChanged = { brightness ->
                    onEvent(StripControlEvent.ChangeBrightness(brightness))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }
    }
}

private class StripControlUiStateProvider : PreviewParameterProvider<StripControlUiState> {
    override val values = sequenceOf(
        StripControlUiState(isConnecting = true),
        StripControlUiState(isConnected = true, isEffectsMenuVisible = true),
        StripControlUiState(
            isConnected = true,
            isEffectsMenuVisible = false,
            selectedEffect = Effect.Fireflies
        )
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