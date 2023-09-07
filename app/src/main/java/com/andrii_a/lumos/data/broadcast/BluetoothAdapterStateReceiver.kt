package com.andrii_a.lumos.data.broadcast

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothAdapterStateReceiver(
    private val onAdapterStateChange: (isEnabled: Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != BluetoothAdapter.ACTION_STATE_CHANGED) {
            return
        }

        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

        when (state) {
            BluetoothAdapter.STATE_ON -> {
                onAdapterStateChange(true)
            }
            BluetoothAdapter.STATE_OFF -> {
                onAdapterStateChange(false)
            }
            else -> Unit
        }
    }
}