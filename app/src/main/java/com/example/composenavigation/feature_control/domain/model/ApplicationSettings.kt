package com.example.composenavigation.feature_control.domain.model

data class ApplicationSettings(
    val deviceAddress: String = "",
    val deviceType: String = "DoubleWay",
    val useTank: Boolean = false,
)
