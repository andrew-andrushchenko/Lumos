package com.andrii_a.lumos.data.mappers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import com.andrii_a.lumos.domain.enums.BluetoothDeviceType
import com.andrii_a.lumos.domain.models.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address,
        type = deviceType
    )
}

private val BluetoothDevice.deviceType: BluetoothDeviceType
    @SuppressLint("MissingPermission")
    get() {
        return when (this.bluetoothClass.deviceClass) {
            BluetoothClass.Device.PHONE_SMART -> BluetoothDeviceType.SMARTPHONE
            BluetoothClass.Device.COMPUTER_LAPTOP -> BluetoothDeviceType.COMPUTER_LAPTOP
            BluetoothClass.Device.COMPUTER_DESKTOP -> BluetoothDeviceType.COMPUTER_DESKTOP
            BluetoothClass.Device.COMPUTER_UNCATEGORIZED -> BluetoothDeviceType.COMPUTER_OTHER
            BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET -> BluetoothDeviceType.HEADSET
            BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE -> BluetoothDeviceType.MICROPHONE
            BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES -> BluetoothDeviceType.HEADPHONES
            BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER -> BluetoothDeviceType.LOUDSPEAKER
            else -> BluetoothDeviceType.OTHER
        }
    }