package com.example.composenavigation.feature_control.domain.use_case.DKController

import android.bluetooth.le.ScanResult
import com.example.composenavigation.feature_control.domain.repository.DKController
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ScanFailure

class StartScanForPeripherals(
    private val dkController: DKController
) {
    operator fun invoke(
        resultCallback: (BluetoothPeripheral, ScanResult) -> Unit,
        scanError: (ScanFailure) -> Unit
    ): String {
        dkController.startScanning(
            resultCallback = resultCallback,
            scanError = scanError
        )
        return "Scan started"
    }
}