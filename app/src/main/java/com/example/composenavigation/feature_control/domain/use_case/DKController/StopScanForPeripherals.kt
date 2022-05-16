package com.example.composenavigation.feature_control.domain.use_case.DKController

import com.example.composenavigation.feature_control.domain.repository.DKController

class StopScanForPeripherals(
    private val dkController: DKController
) {
    operator fun invoke(): String {
        dkController.stopScanning()
        return "Scan stopped"
    }
}