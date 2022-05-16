package com.example.composenavigation.feature_control.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.composenavigation.feature_control.domain.model.ApplicationSettings
import com.example.composenavigation.feature_control.domain.model.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val APPLICATION_PREFERENCES_NAME = "application-preferences"

val Context.applicationDataStore by preferencesDataStore(name = APPLICATION_PREFERENCES_NAME)

class DataStoreManager @Inject constructor(val context: Context) {
    private val appDataStore = context.applicationDataStore

    suspend fun setDeviceAddress(address: String){
        appDataStore.edit { settings ->
            settings[PreferencesKeys.DEVICE_MAC_ADDRESS] = address
        }
    }

    val applicationSettingsFlow: Flow<ApplicationSettings> = appDataStore.data
        .catch { exception ->
            throw exception
        }
        .map { preferences ->
            val deviceAddress = preferences[PreferencesKeys.DEVICE_MAC_ADDRESS]?: ""
            ApplicationSettings(deviceAddress = deviceAddress)
        }
}