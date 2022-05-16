package com.example.composenavigation.feature_control.domain.use_case

import com.example.composenavigation.feature_control.domain.use_case.DKController.*

data class DKControllerUseCases(
    val connectToPeripheral: ConnectToPeripheral,
    val disconnectFromPeripheral: DisconnectFromPeripheral,
    val readSensorsValues: ReadSensorsValues,
    val writeOutputs: WriteOutputs,
    val startScanForPeripherals: StartScanForPeripherals,
    val stopScanForPeripherals: StopScanForPeripherals
)
