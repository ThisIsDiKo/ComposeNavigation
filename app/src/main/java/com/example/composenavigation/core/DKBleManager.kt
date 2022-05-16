package com.example.composenavigation.core

import android.content.Context
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral

class DKBleManager(
    context: Context
) {
    //TODO switch to var bleCentralManager: BluetoothCentralManager = BluetoothCentralManager(context)
    var bleCentralManager: BluetoothCentralManager? = null
    var blePeripheral: BluetoothPeripheral? = null

    init {
        bleCentralManager = BluetoothCentralManager(context)
    }
}