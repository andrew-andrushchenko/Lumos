package com.andrii_a.lumos.domain.models

import com.andrii_a.lumos.domain.enums.BluetoothDeviceType

typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDevice(
    val name: String?,
    val address: String,
    val type: BluetoothDeviceType
)