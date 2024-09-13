package com.rosahosseini.findr.app.di

import com.rosahosseini.findr.app.BuildConfig
import com.rosahosseini.findr.model.BuildConfiguration
import com.rosahosseini.findr.model.Device
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal class AppModule {
    @Provides
    fun provideBuildConfiguration(): BuildConfiguration {
        return BuildConfiguration(
            flickerApiKey = BuildConfig.FLICKR_API_KEY,
            isDebug = BuildConfig.DEBUG,
            device = Device.ANDROID // todo
        )
    }
}
