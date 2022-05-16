package com.example.composenavigation.feature_control.presenter.scan

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenavigation.feature_control.data.DataStoreManager
import com.example.composenavigation.feature_control.domain.use_case.DKControllerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScanScreenViewModel @Inject constructor(
    private val dkControllerUseCases: DKControllerUseCases,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val bleDispatcher = Dispatchers.IO

    private val _bleDevices = mutableStateListOf<BleSimplePeripheral>()
    val bleDevices: SnapshotStateList<BleSimplePeripheral> = _bleDevices

    private val _showConnectionDialogState = mutableStateOf(false)
    val showConnectionDialogState: State<Boolean> = _showConnectionDialogState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val applicationSettingsFlow = dataStoreManager.applicationSettingsFlow

    private var deviceAddress: String = ""

    fun startScanning(){
        //TODO: нужно изменить список, чтобы не ловить ошибку java.util.ConcurrentModificationException
        //Убираем ипользование итератора с индексами
        _bleDevices.clear()
        dkControllerUseCases.startScanForPeripherals(
            resultCallback = {peripheral, scanResult ->
                var index = -1
                for (i in _bleDevices.indices){
                    if (_bleDevices[i].MAC == peripheral.address){
                        index = i
                    }
                }
                //val index = _bleDevices.indexOfFirst { it.MAC == peripheral.address }
                //Timber.i("Found peripheral '${peripheral.address}' with RSSI ${scanResult.rssi}")
                if (index != -1){
                    _bleDevices[index] = BleSimplePeripheral(
                        MAC = peripheral.address,
                        name = peripheral.name,
                        rssi = scanResult.rssi
                    )
                }
                else {
                    _bleDevices.add(BleSimplePeripheral(
                        MAC = peripheral.address,
                        name = peripheral.name,
                        rssi = scanResult.rssi
                    ))
                }
            },
            scanError = {scanFailure ->
                Timber.e("Scan Failed: $scanFailure")
            }
        )    
    }
    
    fun stopScanning(){
        dkControllerUseCases.stopScanForPeripherals()
    }
    fun disconnectFromPeripheral(){
        viewModelScope.launch {
            dkControllerUseCases.disconnectFromPeripheral()
        }
    }

    fun deviceSelected(device: BleSimplePeripheral){
        stopScanning()
        var msg = ""
        Timber.i("connecting to ${device.MAC}")
        _showConnectionDialogState.value = true
        viewModelScope.launch {
            withContext(bleDispatcher){
                dkControllerUseCases.connectToPeripheral(device.MAC)
            }
            Timber.i("connection result: $msg")
            _showConnectionDialogState.value = false
            _eventFlow.emit(
                UiEvent.ShowSnackbar("got message: $msg")
            )
            setDeviceAddress(device.MAC)
            delay(1000)
            _eventFlow.emit(
                UiEvent.NavigateTo("simplescreen")
            )
            //TODO go to next screen, or repeat scanning
        }
    }

    fun setDeviceAddress(address: String){
        viewModelScope.launch {
            dataStoreManager.setDeviceAddress(address)
        }
    }

    fun readApplicationSettings(){
        viewModelScope.launch {
            //TODO collect only when called
            applicationSettingsFlow.collectLatest{
                deviceAddress = it.deviceAddress
                Timber.e("Got address: $deviceAddress")
            }
        }
    }

    sealed class UiEvent{
        data class ShowSnackbar(val message: String): UiEvent()
        data class NavigateTo(val destination: String): UiEvent()
    }
}