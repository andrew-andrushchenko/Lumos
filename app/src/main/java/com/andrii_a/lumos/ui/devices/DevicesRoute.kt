package com.andrii_a.lumos.ui.devices

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.andrii_a.lumos.ui.navigation.Screen
import com.andrii_a.lumos.ui.util.navigateToStripeControl
import com.google.accompanist.systemuicontroller.SystemUiController

fun NavGraphBuilder.devicesRoute(
    navController: NavController,
    systemUiController: SystemUiController
) {
    composable(
        route = Screen.Devices.route,
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
        val systemBarsColor = Color.Transparent
        val areIconsDark = !isSystemInDarkTheme()

        LaunchedEffect(key1 = Unit) {
            systemUiController.setSystemBarsColor(
                color = systemBarsColor,
                darkIcons = areIconsDark
            )
        }

        val viewModel = hiltViewModel<DevicesViewModel>()

        val state by viewModel.state.collectAsStateWithLifecycle()

        DevicesScreen(
            state = state,
            onEvent = viewModel::onEvent,
            navigateToStripePanel = navController::navigateToStripeControl
        )
    }
}