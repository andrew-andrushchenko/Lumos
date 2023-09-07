package com.andrii_a.lumos.di

import android.content.Context
import com.andrii_a.lumos.data.controllers.BluetoothControllerImpl
import com.andrii_a.lumos.domain.controllers.BluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return BluetoothControllerImpl(context)
    }

}