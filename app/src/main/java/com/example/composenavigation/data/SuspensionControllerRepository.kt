package com.example.composenavigation.data

interface SuspensionControllerRepository {
    fun peripheralConnected(): Boolean
}