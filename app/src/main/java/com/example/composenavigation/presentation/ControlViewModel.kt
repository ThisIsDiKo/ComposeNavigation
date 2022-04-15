package com.example.composenavigation.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenavigation.data.SuspensionControllerDataStr
import com.welie.blessed.ConnectionState
import com.welie.blessed.WriteType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class ControlViewModel: ViewModel() {

    private var readingJob: Job? = null

    private val _controllerData = mutableStateOf(SuspensionControllerDataStr())
    val controllerData: State<SuspensionControllerDataStr> = _controllerData

    init{
        viewModelScope.launch {
            BleManager.bleManager?.observeConnectionState { peripheral, state ->
                println("Peripheral ${peripheral.address} change state to $state")
            }
        }
    }

    fun startReadingSensorsValues(){
        readingJob = viewModelScope.launch {
            try{
                while(true){

                    val byteArray = BleManager.blePeripheral?.readCharacteristic(
                        serviceUUID = CONTROL_SERVICE_UUID,
                        characteristicUUID = PRESSURE_CHAR_UUID
                    )?: ByteArray(1)
                    if (byteArray.size == 16){
                        val p1 =  (byteArray[1].toInt() shl 8) + byteArray[0].toInt()
                        val p2 =  (byteArray[3].toInt() shl 8) + byteArray[2].toInt()
                        val p3 =  (byteArray[5].toInt() shl 8) + byteArray[4].toInt()
                        val p4 =  (byteArray[7].toInt() shl 8) + byteArray[6].toInt()
                        val p5 =  (byteArray[9].toInt() shl 8) + byteArray[8].toInt()

                        val p1_bar =  p1.toFloat() / 1000 * 3.45 - 1.725
                        val p2_bar =  p2.toFloat() / 1000 * 3.45 - 1.725
                        val p3_bar =  p3.toFloat() / 1000 * 3.45 - 1.725
                        val p4_bar =  p4.toFloat() / 1000 * 3.45 - 1.725
                        val p5_bar =  p5.toFloat() / 1000 * 3.45 - 1.725

                        _controllerData.value = controllerData.value.copy(
                            pressure1 = String.format("%.1f", p1_bar),
                            pressure2 = String.format("%.1f", p2_bar),
                            pressure3 = String.format("%.1f", p3_bar),
                            pressure4 = String.format("%.1f", p4_bar),
                            pressureTank = String.format("%.1f", p5_bar),

                            )
                    }
                    delay(500)
                }
            }
            catch(e: Exception){
                println("ERROR: Got exception in reading: ${e.toString()}")
            }
            finally {
                println("finally job canceled")
                readingJob = null
            }
        }

    }

    fun stopReadingSensorsValues(){
        if (readingJob != null){
            println("cancelling Job")
            readingJob?.cancel()
            readingJob = null
        }
    }

    fun writeOutputCharacteristic(s: String){
        viewModelScope.launch {
            if(BleManager.blePeripheral?.getState() == ConnectionState.CONNECTED){
                BleManager.blePeripheral?.writeCharacteristic(
                    serviceUUID = CONTROL_SERVICE_UUID,
                    characteristicUUID = VALVE_CHAR_UUID,
                    value = s.toByteArray(),
                    writeType = WriteType.WITH_RESPONSE
                )
            }

        }
    }

    companion object{
        val CONTROL_SERVICE_UUID = UUID.fromString("d2309610-80dd-11ec-a8a3-0242ac120002")
        val VALVE_CHAR_UUID = UUID.fromString("d2309611-80dd-11ec-a8a3-0242ac120002")
        val PRESSURE_CHAR_UUID = UUID.fromString("d2309612-80dd-11ec-a8a3-0242ac120002")
    }
}