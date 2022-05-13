package com.example.composenavigation.feature_control.domain.model

data class SensorsRawValuesModel(
    val pressure1: Int = 0,
    val pressure2: Int = 0,
    val pressure3: Int = 0,
    val pressure4: Int = 0,
    val pressure5: Int = 0,

    val pos1: Int = 0,
    val pos2: Int = 0,
    val pos3: Int = 0,
    val pos4: Int = 0,

    val error: Boolean = false,
    val isCalibrating: Boolean = false,
    val isAligning: Boolean = false
){
    fun convertToPressureUnits(): SensorsModel{
        return SensorsModel(
            pressure1 = pressure1.toDouble(),
            pressure2 = pressure2.toDouble(),
            pressure3 = pressure3.toDouble(),
            pressure4 = pressure4.toDouble(),
            pressure5 = pressure5.toDouble(),
        )
    }
}
