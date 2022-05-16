package com.example.composenavigation.feature_control.domain.repository

import android.bluetooth.le.ScanResult
import arrow.core.Either
import com.example.composenavigation.feature_control.data.BleCommunicationError
import com.example.composenavigation.feature_control.domain.model.OutputsModel
import com.example.composenavigation.feature_control.domain.model.SensorsModel
import com.example.composenavigation.feature_control.domain.model.SensorsRawValuesModel
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ConnectionState
import com.welie.blessed.ScanFailure

interface DKController {
    fun createPeripheral(mac: String): BluetoothPeripheral?
    suspend fun writeOutputs(outputs: OutputsModel): Either<BleCommunicationError, Unit>
    suspend fun readSensors(): Either<BleCommunicationError, SensorsModel>
    suspend fun connect(peripheral: BluetoothPeripheral): Either<BleCommunicationError, Unit>
    suspend fun disconnect(peripheral: BluetoothPeripheral): Either<BleCommunicationError, Unit>
    fun observerConnectionStatus(connectionCallback: (peripheral : BluetoothPeripheral, state : ConnectionState) -> Unit)
    fun getCurrentPeripheral(): BluetoothPeripheral?
    fun startScanning(resultCallback: (BluetoothPeripheral, ScanResult) -> Unit, scanError: (ScanFailure) -> Unit)
    fun stopScanning()
}