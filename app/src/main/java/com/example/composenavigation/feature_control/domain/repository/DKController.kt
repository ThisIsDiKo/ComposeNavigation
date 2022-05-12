package com.example.composenavigation.feature_control.domain.repository

import com.example.composenavigation.feature_control.domain.model.OutputsModel
import com.example.composenavigation.feature_control.domain.model.SensorsModel
import com.example.composenavigation.feature_control.domain.model.SensorsRawValuesModel

interface DKController {
    suspend fun writeOutputs(outputs: OutputsModel)
    suspend fun readSensors(): SensorsModel
    suspend fun connect()
    suspend fun disconnect()
    fun observerConnectionStatus(callback: (Unit) -> Unit)
}