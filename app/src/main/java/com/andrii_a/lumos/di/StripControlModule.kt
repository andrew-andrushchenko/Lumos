package com.andrii_a.lumos.di

import com.andrii_a.lumos.ui.strip_control.StripControlViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val stripControlModule = module {
    viewModelOf(::StripControlViewModel)
}