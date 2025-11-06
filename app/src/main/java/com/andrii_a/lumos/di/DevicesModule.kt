package com.andrii_a.lumos.di

import com.andrii_a.lumos.ui.devices.DevicesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val devicesModule = module {
    viewModelOf(::DevicesViewModel)
}