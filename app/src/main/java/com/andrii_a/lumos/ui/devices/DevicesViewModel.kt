package com.andrii_a.lumos.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrii_a.lumos.domain.controllers.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed interface DevicesScreenEvent {
    data object StartDeviceScan : DevicesScreenEvent
    data object StopDeviceScan : DevicesScreenEvent
}

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
) : ViewModel() {

    private val _state = MutableStateFlow(DevicesUiState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = _state.value
    )

    init {
        bluetoothController.isBluetoothEnabled.onEach { isEnabled ->
            _state.update { it.copy(isBluetoothEnabled = isEnabled) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: DevicesScreenEvent) {
        when (event) {
            is DevicesScreenEvent.StartDeviceScan -> {
                startScan()
            }

            is DevicesScreenEvent.StopDeviceScan -> {
                stopScan()
            }
        }
    }

    private fun startScan() {
        bluetoothController.startDiscovery()
        _state.update {
            it.copy(isScanning = true)
        }
    }

    private fun stopScan() {
        bluetoothController.stopDiscovery()
        _state.update {
            it.copy(isScanning = false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }
}