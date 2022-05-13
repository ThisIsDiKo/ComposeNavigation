package com.example.composenavigation.feature_control.data


sealed class BleCommunicationError() {
    data class InvalidSensorsDataPacketError(val msg: String): BleCommunicationError()
    data class UnknownError(val msg: String): BleCommunicationError()
}