package com.andrii_a.lumos.ui.util

import androidx.navigation.NavController
import com.andrii_a.lumos.domain.models.BluetoothDeviceDomain
import com.andrii_a.lumos.ui.navigation.Screen

fun NavController.navigateToStripControl(device: BluetoothDeviceDomain) {
    this.navigate(Screen.StripControl(device.address))
}