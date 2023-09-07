package com.andrii_a.lumos.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.Headset
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.ScreenSearchDesktop
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Speaker
import androidx.compose.ui.graphics.vector.ImageVector
import com.andrii_a.lumos.domain.enums.BluetoothDeviceType

val BluetoothDeviceType.icon: ImageVector
    get() {
        return when(this) {
            BluetoothDeviceType.SMARTPHONE -> Icons.Outlined.Smartphone
            BluetoothDeviceType.COMPUTER_LAPTOP -> Icons.Outlined.Laptop
            BluetoothDeviceType.COMPUTER_DESKTOP -> Icons.Outlined.DesktopWindows
            BluetoothDeviceType.COMPUTER_OTHER -> Icons.Outlined.ScreenSearchDesktop
            BluetoothDeviceType.LOUDSPEAKER -> Icons.Outlined.Speaker
            BluetoothDeviceType.HEADSET -> Icons.Outlined.HeadsetMic
            BluetoothDeviceType.HEADPHONES -> Icons.Outlined.Headset
            BluetoothDeviceType.MICROPHONE -> Icons.Outlined.Mic
            BluetoothDeviceType.OTHER -> Icons.Outlined.Bluetooth
        }
    }