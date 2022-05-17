package com.example.composenavigation.feature_control.presenter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenavigation.feature_control.data.BleCommunicationError
import com.example.composenavigation.feature_control.data.DataStoreManager
import com.example.composenavigation.feature_control.domain.model.OutputsModel
import com.example.composenavigation.feature_control.domain.use_case.DKControllerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SimpleScreenViewModel @Inject constructor(
    private val dkControllerUseCases: DKControllerUseCases,
    private val dataStoreManager: DataStoreManager
): ViewModel() {


    private val _messageContent = mutableStateOf("Empty")
    val messageContent: State<String> = _messageContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val applicationSettingsFlow = dataStoreManager.applicationSettingsFlow
    private var deviceAddress = ""

    override fun onCleared() {
        super.onCleared()
        Timber.e("Simple screen view model cleared")
    }

    init {
        viewModelScope.launch {
            //TODO collect only when called
            //val settings = applicationSettingsFlow.last()
            applicationSettingsFlow.collectLatest{
                deviceAddress = it.deviceAddress
                Timber.e("Got address: $deviceAddress")
                if (deviceAddress.isBlank()){
                    _eventFlow.emit(
                        UiEvent.NavigateTo("scanscreen")
                    )
                }

            }
        }
    }

    fun onConnectClicked(){
        viewModelScope.launch {
           dkControllerUseCases.connectToPeripheral("DA:3A:0B:BA:D1:CF")
                .fold(
                    {error ->
                        _messageContent.value = when(error){
                            is BleCommunicationError.UnknownError -> {
                                error.msg
                            }
                            else -> {
                                "Unknown error"
                            }
                        }
                    },
                    {
                        _messageContent.value = "Peripheral connected"
                    }
                )
        }
    }

    fun onDisconnectClicked(){
        viewModelScope.launch {
            _messageContent.value = dkControllerUseCases.disconnectFromPeripheral()
        }
    }

    fun onReadClicked(){
        viewModelScope.launch {
            _messageContent.value = dkControllerUseCases.readSensorsValues()
        }
    }

    fun onStartScanClicked(){
        _messageContent.value = dkControllerUseCases.startScanForPeripherals(
            resultCallback = { peripheral, scanResult ->
                Timber.i("Found peripheral '${peripheral.name}' '${peripheral.address}' with RSSI ${scanResult.rssi}")
            },
            scanError = { scanFailure ->
                Timber.e("scan failed with reason $scanFailure")
            }
        )
    }

    fun onStopScanClicked(){
        _messageContent.value = dkControllerUseCases.stopScanForPeripherals()
    }

    fun onWriteClicked(){
        viewModelScope.launch {
            val outputs = OutputsModel(10)
            outputs.setOutput(0, true)
            outputs.setOutput(2, true)
            outputs.setOutput(4, true)
            outputs.setOutput(6, true)
            outputs.setOutput(8, true)
            outputs.setOutput(9, true)
            _messageContent.value = dkControllerUseCases.writeOutputs(outputs)
        }
    }

    fun onClearDeviceAddressClicked(){
        viewModelScope.launch {
            dataStoreManager.setDeviceAddress("")
        }
    }

    sealed class UiEvent{
        data class NavigateTo(val destination: String): UiEvent()
    }
}