package com.andrii_a.lumos.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrii_a.lumos.domain.controllers.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val navigationEventChannel = Channel<DevicesNavigationEvent>()
    val navigationEventFlow = navigationEventChannel.receiveAsFlow()

    init {
        bluetoothController.isBluetoothEnabled.onEach { isEnabled ->
            _state.update { it.copy(isBluetoothEnabled = isEnabled) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: DevicesEvent) {
        when (event) {
            is DevicesEvent.StartDeviceScan -> {
                startScan()
            }

            is DevicesEvent.StopDeviceScan -> {
                stopScan()
            }

            is DevicesEvent.SelectDevice -> {
                viewModelScope.launch {
                    navigationEventChannel.send(DevicesNavigationEvent.NavigateToStripControl(event.device))
                }
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