package com.andrii_a.lumos.ui.stripe_control

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrii_a.lumos.domain.controllers.BluetoothController
import com.andrii_a.lumos.domain.controllers.ConnectionResult
import com.andrii_a.lumos.domain.enums.BluetoothDeviceType
import com.andrii_a.lumos.domain.models.BluetoothDeviceDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "StripeControlViewModel"

sealed interface StripeControlScreenEvent {
    data class ConnectToDevice(val address: String) : StripeControlScreenEvent
    data object DisconnectFromDevice : StripeControlScreenEvent
    data class ChangeColor(val colorHSV: Triple<Float, Float, Float>) : StripeControlScreenEvent
    data class ChangeBrightness(val brightness: Float) : StripeControlScreenEvent
}

@HiltViewModel
class StripeControlViewModel @Inject constructor(
    private val bluetoothController: BluetoothController,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(StripeControlUiState())
    val state = _state.asStateFlow()

    private var deviceConnectionJob: Job? = null

    init {
        bluetoothController.isBluetoothEnabled.onEach { isEnabled ->
            _state.update { it.copy(isBluetoothEnabled = isEnabled) }
        }.launchIn(viewModelScope)

        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update { it.copy(errorMessage = error) }
        }.launchIn(viewModelScope)

        savedStateHandle.get<String>("address")?.let { address ->
            onEvent(StripeControlScreenEvent.ConnectToDevice(address))
        }
    }

    fun onEvent(event: StripeControlScreenEvent) {
        when (event) {
            is StripeControlScreenEvent.ConnectToDevice -> {
                val device = BluetoothDeviceDomain(
                    name = null,
                    address = event.address,
                    type = BluetoothDeviceType.OTHER
                )

                connectToDevice(device)
            }

            is StripeControlScreenEvent.DisconnectFromDevice -> {
                disconnectFromDevice()
            }

            is StripeControlScreenEvent.ChangeBrightness -> {
                sendMessage("b${event.brightness.toInt()}")
            }

            is StripeControlScreenEvent.ChangeColor -> {
                sendMessage("c${event.colorHSV.first}")
            }
        }
    }

    private fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }

        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    private fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()

        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }

        Log.d(TAG, "Connection finished.")
    }

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            val bluetoothMessage = bluetoothController.trySendMessage(message)

            bluetoothMessage?.let { message ->
                Log.d(TAG, "Sent message: $message")
            }
        }
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null
                        )
                    }

                    Log.d(TAG, "Connection established.")
                }

                is ConnectionResult.TransferSucceeded -> {
                    Log.d(TAG, "Obtained message: ${result.message}")
                }

                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }.catch {
            bluetoothController.closeConnection()
            _state.update {
                it.copy(
                    isConnected = false,
                    isConnecting = false,
                )
            }
        }.launchIn(viewModelScope)
    }
}