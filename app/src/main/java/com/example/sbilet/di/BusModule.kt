package com.example.sbilet.di

import com.example.sbilet.config.SBiletReqeustExecutor
import com.example.sbilet.modules.main.bus.BusRepository
import com.example.sbilet.modules.splash.SplashRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BusModule {
    @Provides
    @Singleton
    fun provideBusRepository(
        sBiletReqeustExecutor: SBiletReqeustExecutor
    ) = BusRepository(
        sBiletReqeustExecutor
    )
}
