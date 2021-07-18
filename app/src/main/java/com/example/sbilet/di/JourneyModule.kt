package com.example.sbilet.di

import com.example.sbilet.config.SBiletReqeustExecutor
import com.example.sbilet.modules.journeys.JourneyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JourneyModule {
    @Provides
    @Singleton
    fun provideJourneyRepository(
        sBiletReqeustExecutor: SBiletReqeustExecutor
    ) = JourneyRepository(
        sBiletReqeustExecutor
    )
}
