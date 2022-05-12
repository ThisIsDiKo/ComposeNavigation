package com.example.composenavigation.feature_control.data

import com.example.composenavigation.core.DKBleManager
import com.example.composenavigation.feature_control.domain.model.InvalidReceivedData
import com.example.composenavigation.feature_control.domain.model.OutputsModel
import com.example.composenavigation.feature_control.domain.model.SensorsModel
import com.example.composenavigation.feature_control.domain.model.SensorsRawValuesModel
import com.example.composenavigation.feature_control.domain.repository.DKController
import timber.log.Timber

class DKControllerImpl(
    val bleManager: DKBleManager
): DKController {
    override suspend fun writeOutputs(outputs: OutputsModel) {
        TODO("Not yet implemented")
    }


    //TODO: Need to use Either or something else to return error if disconnect
    override suspend fun readSensors(): SensorsModel {
        try{
            val rawSensorsData = readRawSensorsData()
            return SensorsModel()
        }
        catch(e: Exception){
            when(e){
                is InvalidReceivedData -> Timber.e("Got invalid data packet: $e")
                else -> Timber.e("Error while reading packet: $e")
            }

        }
        finally {
            println("finally job canceled")
            return SensorsModel()
        }
    }

    @Throws(InvalidReceivedData::class)
    suspend fun readRawSensorsData(): SensorsRawValuesModel{
        val byteArray = bleManager.blePeripheral?.readCharacteristic(
            serviceUUID = ServiceUUID.CONTROL_SERVICE_UUID,
            characteristicUUID = ServiceUUID.PRESSURE_CHAR_UUID
        )?: ByteArray(1)

        if (byteArray.size == CommunicationParameters.sensorsPacketLength){
            return SensorsRawValuesModel()
        }
        else {
            throw InvalidReceivedData("Got ${byteArray.size} bytes instead of 16")
        }
    }
}