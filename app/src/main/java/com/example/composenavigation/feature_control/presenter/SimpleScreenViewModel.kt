package com.example.composenavigation.feature_control.presenter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenavigation.feature_control.domain.model.OutputsModel
import com.example.composenavigation.feature_control.domain.use_case.DKControllerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SimpleScreenViewModel @Inject constructor(
    private val dkControllerUseCases: DKControllerUseCases
): ViewModel() {
    private val _messageContent = mutableStateOf("Empty")
    val messageContent: State<String> = _messageContent

    fun onConnectClicked(){
        viewModelScope.launch {
            _messageContent.value = dkControllerUseCases.connectToPeripheral("DA:3A:0B:BA:D1:CF")
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
}