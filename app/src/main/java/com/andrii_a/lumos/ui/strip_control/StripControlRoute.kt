package com.andrii_a.lumos.ui.strip_control

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.andrii_a.lumos.ui.navigation.Screen

fun NavGraphBuilder.stripControlRoute(navController: NavController) {
    composable<Screen.StripControl>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(500)
            )
        }
    ) {
        val viewModel = hiltViewModel<StripControlViewModel>()

        val state by viewModel.state.collectAsStateWithLifecycle()

        val context = LocalContext.current
        LaunchedEffect(state) {
            state.errorMessage?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            val isDisconnected = !state.isConnecting && !state.isConnected
            if (isDisconnected || !state.isBluetoothEnabled) {
                navController.navigateUp()
            }
        }

        StripControlScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}