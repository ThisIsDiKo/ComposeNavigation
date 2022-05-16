package com.example.composenavigation.feature_control.data

import android.bluetooth.le.ScanResult
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.composenavigation.core.DKBleManager
import com.example.composenavigation.feature_control.domain.model.InvalidReceivedData
import com.example.composenavigation.feature_control.domain.model.OutputsModel
import com.example.composenavigation.feature_control.domain.model.SensorsModel
import com.example.composenavigation.feature_control.domain.model.SensorsRawValuesModel
import com.example.composenavigation.feature_control.domain.repository.DKController
import com.example.composenavigation.presentation.BleManager
import com.example.composenavigation.presentation.ControlViewModel
import com.welie.blessed.*
import timber.log.Timber

class DKControllerImpl(
    val bleManager: DKBleManager
): DKController {

    override fun createPeripheral(mac: String): BluetoothPeripheral? {
        return bleManager.bleCentralManager?.getPeripheral(mac)
    }

    override suspend fun writeOutputs(outputs: OutputsModel): Either<BleCommunicationError, Unit> {
        return try {
            bleManager.blePeripheral?.writeCharacteristic(
                serviceUUID = ServiceUUID.CONTROL_SERVICE_UUID,
                characteristicUUID = ServiceUUID.VALVE_CHAR_UUID,
                value = outputs.convertOutputsToBytes(),
                writeType = WriteType.WITH_RESPONSE
            ) ?: BleCommunicationError.UnknownError("peripheral is null").left()
            Unit.right()
        }
        catch (e: Exception){
            BleCommunicationError.UnknownError(e.message ?: "").left()
        }
    }

    override suspend fun readSensors(): Either<BleCommunicationError, SensorsModel> {
        return try{
            val rawSensorsData = readRawSensorsData()
            rawSensorsData.convertToPressureUnits().right()
        } catch(e: Exception){
            when(e){
                is InvalidReceivedData -> {
                    BleCommunicationError.InvalidSensorsDataPacketError(e.message ?: "").left()
                }
                else ->{
                    BleCommunicationError.UnknownError(e.message ?: "").left()
                }
            }
        }
    }

    override suspend fun connect(peripheral: BluetoothPeripheral): Either<BleCommunicationError, Unit> {
        try {
            bleManager.bleCentralManager?.connectPeripheral(peripheral)
            bleManager.blePeripheral = peripheral
            return Unit.right()
        } catch (e: Exception){
           return BleCommunicationError.UnknownError(e.toString()).left()
        }
    }

    override suspend fun disconnect(peripheral: BluetoothPeripheral): Either<BleCommunicationError, Unit> {
        return try {
            bleManager.bleCentralManager?.cancelConnection(peripheral)
            bleManager.blePeripheral = null
            Unit.right()
        } catch (e: Exception){
            BleCommunicationError.UnknownError(e.toString()).left()
        }
    }

    override fun observerConnectionStatus(connectionCallback: (peripheral : BluetoothPeripheral, state : ConnectionState) -> Unit) {
        bleManager.bleCentralManager?.observeConnectionState{peripheral, state ->
            connectionCallback(peripheral, state)
        }
    }

    override fun getCurrentPeripheral(): BluetoothPeripheral?{
        return bleManager.blePeripheral
    }

    override fun startScanning(
        resultCallback: (BluetoothPeripheral, ScanResult) -> Unit,
        scanError: (ScanFailure) -> Unit
    ) {
        bleManager.bleCentralManager?.setScanMode(ScanMode.BALANCED)
        bleManager.bleCentralManager?.scanForPeripherals(
            resultCallback = resultCallback,
            scanError = scanError
        )
    }

    override fun stopScanning() {
        bleManager.bleCentralManager?.stopScan()
    }

    @Throws(InvalidReceivedData::class)
    suspend fun readRawSensorsData(): SensorsRawValuesModel{
        val byteArray = bleManager.blePeripheral?.readCharacteristic(
            serviceUUID = ServiceUUID.CONTROL_SERVICE_UUID,
            characteristicUUID = ServiceUUID.PRESSURE_CHAR_UUID
        )?: ByteArray(1)

        if (byteArray.size == CommunicationParameters.sensorsPacketLength){
            val p1 =  (byteArray[1].toInt() shl 8) + byteArray[0].toInt()
            val p2 =  (byteArray[3].toInt() shl 8) + byteArray[2].toInt()
            val p3 =  (byteArray[5].toInt() shl 8) + byteArray[4].toInt()
            val p4 =  (byteArray[7].toInt() shl 8) + byteArray[6].toInt()
            val p5 =  (byteArray[9].toInt() shl 8) + byteArray[8].toInt()
            return SensorsRawValuesModel(
                pressure1 = p1,
                pressure2 = p2,
                pressure3 = p3,
                pressure4 = p4,
                pressure5 = p5,
            )
        }
        else {
            throw InvalidReceivedData("Got ${byteArray.size} bytes instead of 16")
        }
    }


}