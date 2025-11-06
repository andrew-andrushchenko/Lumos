package com.andrii_a.lumos.di

import com.andrii_a.lumos.data.controllers.BluetoothControllerImpl
import com.andrii_a.lumos.domain.controllers.BluetoothController
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val bluetoothModule = module {
    singleOf(::BluetoothControllerImpl) { bind<BluetoothController>() }
}
