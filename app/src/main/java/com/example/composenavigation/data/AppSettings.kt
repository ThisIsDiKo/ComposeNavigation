package com.example.composenavigation.data

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val deviceAddress: String = "DA:3A:0B:BA:D1:CF",
    val deviceType: String = "DoubleWay",
    val useTank: Boolean = false,
    val pressureUnits: PressureUnits = PressureUnits.BAR,
    val pressureSensType: PressureSensorType = PressureSensorType.CATLEPILLAR_0_14

)

enum class PressureUnits{
    BAR, PSI
}

enum class PressureSensorType{
    CATLEPILLAR_0_14,
    CHINA_0_20
}