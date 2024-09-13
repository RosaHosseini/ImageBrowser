package com.rosahosseini.findr.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class HttpEngineModule {

    @Singleton
    @Provides
    fun provideHttpEngine(): HttpClientEngine = OkHttp.create()
}
