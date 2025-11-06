package com.andrii_a.lumos.ui.strip_control

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.andrii_a.lumos.domain.CommProtocol
import com.andrii_a.lumos.domain.controllers.BluetoothController
import com.andrii_a.lumos.domain.controllers.ConnectionResult
import com.andrii_a.lumos.domain.enums.BluetoothDeviceType
import com.andrii_a.lumos.domain.models.BluetoothDeviceDomain
import com.andrii_a.lumos.ui.navigation.Screen
import com.andrii_a.lumos.ui.strip_control.effects.Effect
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "StripControlViewModel"

class StripControlViewModel(
    private val bluetoothController: BluetoothController,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(StripControlUiState())
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

        val address = savedStateHandle.toRoute<Screen.StripControl>().btDeviceAddress
        onEvent(StripControlEvent.ConnectToDevice(address))
    }

    fun onEvent(event: StripControlEvent) {
        when (event) {
            is StripControlEvent.ConnectToDevice -> {
                val device = BluetoothDeviceDomain(
                    name = null,
                    address = event.address,
                    type = BluetoothDeviceType.OTHER
                )

                connectToDevice(device)
            }

            is StripControlEvent.DisconnectFromDevice -> {
                disconnectFromDevice()
            }

            is StripControlEvent.ShowEffectsMenu -> {
                _state.update {
                    it.copy(
                        isEffectsMenuVisible = true,
                        selectedEffect = Effect.None
                    )
                }
            }

            is StripControlEvent.ChangeEffect -> {
                val changeEffectInstruction = CommProtocol.changeEffect(event.effect.id)
                sendMessage(changeEffectInstruction)

                _state.update {
                    it.copy(
                        isEffectsMenuVisible = false,
                        selectedEffect = event.effect,
                        lastSentInstruction = changeEffectInstruction
                    )
                }
            }

            is StripControlEvent.ChangeBrightness -> {
                val changeBrightnessInstruction =
                    CommProtocol.changeBrightness(event.brightness.toInt())
                sendMessage(changeBrightnessInstruction)

                _state.update {
                    it.copy(lastSentInstruction = changeBrightnessInstruction)
                }
            }

            is StripControlEvent.ChangeFireplaceColor -> {
                val changeFireplaceColorInstruction =
                    CommProtocol.changeFireplaceColor(event.colorHexString)
                sendMessage(changeFireplaceColorInstruction)

                _state.update {
                    it.copy(lastSentInstruction = changeFireplaceColorInstruction)
                }
            }

            is StripControlEvent.ChangeFirefliesAmount -> {
                val changeFirefliesAmountInstruction =
                    CommProtocol.changeFirefliesAmount(event.amount)
                sendMessage(changeFirefliesAmountInstruction)

                _state.update {
                    it.copy(lastSentInstruction = changeFirefliesAmountInstruction)
                }
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

                    Log.d(TAG, "ListenerFlow: Bluetooth connection established.")
                }

                is ConnectionResult.TransferSucceeded -> {
                    _state.update {
                        it.copy(
                            receivedMessages = it.receivedMessages + result.message
                        )
                    }

                    Log.d(TAG, "ListenerFlow: Obtained message: ${result.message}")
                }

                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                    Log.e(TAG, "ListenerFlow: Transfer error: ${result.message}")
                }
            }
        }.catch { throwable ->
            bluetoothController.closeConnection()
            _state.update {
                it.copy(
                    isConnected = false,
                    isConnecting = false,
                )
            }
            Log.e(TAG, "ListenerFlow: Connection interrupted.", throwable)
        }.launchIn(viewModelScope)
    }
}