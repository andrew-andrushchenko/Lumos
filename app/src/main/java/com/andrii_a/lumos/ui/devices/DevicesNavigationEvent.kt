package com.andrii_a.lumos.ui.devices

import com.andrii_a.lumos.domain.models.BluetoothDeviceDomain

sealed interface DevicesNavigationEvent {

    data class NavigateToStripControl(val device: BluetoothDeviceDomain) : DevicesNavigationEvent
}