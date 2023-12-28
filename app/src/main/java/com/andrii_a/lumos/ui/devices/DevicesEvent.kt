package com.andrii_a.lumos.ui.devices

sealed interface DevicesEvent {
    data object StartDeviceScan : DevicesEvent
    data object StopDeviceScan : DevicesEvent
}