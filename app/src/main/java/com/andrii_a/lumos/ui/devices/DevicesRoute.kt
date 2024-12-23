package com.andrii_a.lumos.ui.devices

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.andrii_a.lumos.ui.navigation.Screen
import com.andrii_a.lumos.ui.util.collectAsOneTimeEvents

fun NavGraphBuilder.devicesRoute(navController: NavController) {
    composable<Screen.Devices>(
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            )
        }
    ) {
        val viewModel = hiltViewModel<DevicesViewModel>()

        val state by viewModel.state.collectAsStateWithLifecycle()

        viewModel.navigationEventFlow.collectAsOneTimeEvents { event ->
            when (event) {
                is DevicesNavigationEvent.NavigateToStripControl -> {
                    navController.navigate(Screen.StripControl(event.device.address))
                }
            }
        }

        DevicesScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}