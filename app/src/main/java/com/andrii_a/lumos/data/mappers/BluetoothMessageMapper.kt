package com.andrii_a.lumos.data.mappers

import com.andrii_a.lumos.domain.models.BluetoothMessage

fun String.toBluetoothMessage(): BluetoothMessage {
    return BluetoothMessage(value = this)
}

fun BluetoothMessage.toByteArray(): ByteArray {
    return value.encodeToByteArray()
}