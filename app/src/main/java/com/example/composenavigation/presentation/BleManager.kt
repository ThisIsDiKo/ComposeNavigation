package com.example.composenavigation.presentation

import android.annotation.SuppressLint
import android.content.Context
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import timber.log.Timber


object BleManager {
    @SuppressLint("StaticFieldLeak")
    var bleManager: BluetoothCentralManager? = null
    @SuppressLint("StaticFieldLeak")
    var blePeripheral: BluetoothPeripheral? = null

    fun getInstance(context: Context): BluetoothCentralManager?{
        bleManager = BluetoothCentralManager(context)
        if (bleManager == null) {
            bleManager = BluetoothCentralManager(context)
        }

        return bleManager;
    }

}