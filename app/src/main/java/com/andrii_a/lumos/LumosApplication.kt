package com.andrii_a.lumos

import android.app.Application
import com.andrii_a.lumos.di.bluetoothModule
import com.andrii_a.lumos.di.devicesModule
import com.andrii_a.lumos.di.stripControlModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LumosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LumosApplication)
            modules(
                bluetoothModule,
                devicesModule,
                stripControlModule
            )
        }
    }

}