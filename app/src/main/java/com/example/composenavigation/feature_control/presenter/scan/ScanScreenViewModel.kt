package com.example.composenavigation.feature_control.presenter.scan

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.composenavigation.feature_control.domain.use_case.DKControllerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScanScreenViewModel @Inject constructor(
    private val dkControllerUseCases: DKControllerUseCases
): ViewModel() {


    private val _bleDevices = mutableStateListOf<BleSimplePeripheral>()
    val bleDevices: SnapshotStateList<BleSimplePeripheral> = _bleDevices

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
}