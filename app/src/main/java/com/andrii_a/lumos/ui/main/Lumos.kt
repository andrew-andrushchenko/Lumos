package com.andrii_a.lumos.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.andrii_a.lumos.ui.navigation.MainNavigationHost
import com.andrii_a.lumos.ui.permissions.PermissionsRequiredBanner
import com.andrii_a.lumos.ui.theme.LumosTheme

@Composable
fun Lumos() {
    LumosTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val (arePermissionsGranted, onPermissionsRequested) = remember {
                mutableStateOf(false)
            }

            if (arePermissionsGranted) {
                val navHostController = rememberNavController()

                MainNavigationHost(
                    navHostController = navHostController,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                PermissionsRequiredBanner(onPermissionsRequested = onPermissionsRequested)
            }
        }
    }
}
