package com.example.composenavigation.feature_control.domain.use_case

import android.bluetooth.le.ScanResult
import com.example.composenavigation.feature_control.domain.repository.DKController
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ScanFailure

class StopScanForPeripherals(
    private val dkController: DKController
) {
    operator fun invoke(): String {
        dkController.stopScanning()
        return "Scan stopped"
    }
}