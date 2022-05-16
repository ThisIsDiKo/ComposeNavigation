package com.example.composenavigation.feature_control.domain.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val DEVICE_MAC_ADDRESS = stringPreferencesKey("device_mac_address")
    val DEVICE_TYPE = stringPreferencesKey("device_type")
    val SHOW_PRESSURE_IN_TANK = booleanPreferencesKey("show_pressure_in_tank")
}