package com.andrii_a.lumos.ui.devices

import com.andrii_a.lumos.domain.models.BluetoothDeviceDomain

data class DevicesUiState(
    val isBluetoothEnabled: Boolean = false,
    val scannedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val pairedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val isScanning: Boolean = false
)