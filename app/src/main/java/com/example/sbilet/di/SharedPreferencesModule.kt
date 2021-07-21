package com.example.sbilet.di

import android.content.Context
import com.example.sbilet.util.QueryPreferencesHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {
    @Provides
    @Singleton
    fun provideQueryPreferencesHelper(
        @ApplicationContext context: Context,
        gson: Gson
    ) = QueryPreferencesHelper(
        context,
        gson
    )

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
}
