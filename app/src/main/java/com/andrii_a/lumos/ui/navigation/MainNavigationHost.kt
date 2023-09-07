package com.andrii_a.lumos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.systemuicontroller.SystemUiController

@Composable
fun MainNavigationHost(
    navHostController: NavHostController,
    systemUiController: SystemUiController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Devices.route,
        modifier = modifier
    ) {

    }
}