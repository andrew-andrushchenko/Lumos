package com.andrii_a.lumos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.andrii_a.lumos.ui.devices.devicesRoute
import com.andrii_a.lumos.ui.stripe_control.stripeControlRoute

@Composable
fun MainNavigationHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Devices.route,
        modifier = modifier
    ) {
        devicesRoute(navHostController)
        stripeControlRoute(navHostController)
    }
}