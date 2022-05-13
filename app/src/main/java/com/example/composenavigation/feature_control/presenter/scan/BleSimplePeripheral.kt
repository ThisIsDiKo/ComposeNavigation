package com.example.composenavigation.feature_control.presenter.scan

data class BleSimplePeripheral(
    val MAC: String = "",
    val name: String = "",
    var rssi: Int = 0
)