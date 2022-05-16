package com.example.composenavigation.di

import android.content.Context
import com.example.composenavigation.core.DKBleManager
import com.example.composenavigation.feature_control.data.DKControllerImpl
import com.example.composenavigation.feature_control.data.DataStoreManager
import com.example.composenavigation.feature_control.domain.repository.DKController
import com.example.composenavigation.feature_control.domain.use_case.*
import com.example.composenavigation.feature_control.domain.use_case.DKController.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBleCentralManager(@ApplicationContext appContext: Context): DKBleManager {
        return DKBleManager(appContext)
    }

    @Provides
    @Singleton
    fun provideDkController(bleManager: DKBleManager): DKController{
        return DKControllerImpl(bleManager)
    }

    @Provides
    @Singleton
    fun provideDKControllerUseCases(dkController: DKController): DKControllerUseCases{
        return DKControllerUseCases(
            connectToPeripheral = ConnectToPeripheral(dkController),
            disconnectFromPeripheral = DisconnectFromPeripheral(dkController),
            readSensorsValues = ReadSensorsValues(dkController),
            writeOutputs = WriteOutputs(dkController),
            startScanForPeripherals = StartScanForPeripherals(dkController),
            stopScanForPeripherals = StopScanForPeripherals(dkController)
        )
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext appContext: Context): DataStoreManager{
        return DataStoreManager(context = appContext)
    }
}