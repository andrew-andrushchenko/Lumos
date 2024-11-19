package com.andrii_a.lumos.ui.devices

import com.andrii_a.lumos.domain.models.BluetoothDeviceDomain

sealed interface DevicesEvent {

    data object StartDeviceScan : DevicesEvent

    data object StopDeviceScan : DevicesEvent

    data class SelectDevice(val device: BluetoothDeviceDomain) : DevicesEvent
}