package com.example.composenavigation.di

import android.content.Context
import com.example.composenavigation.core.DKBleManager
import com.example.composenavigation.feature_control.data.DKControllerImpl
import com.example.composenavigation.feature_control.domain.repository.DKController
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
}