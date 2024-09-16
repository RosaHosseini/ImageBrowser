package com.rosahosseini.findr.app.di

import com.rosahosseini.findr.app.BuildConfig
import com.rosahosseini.findr.model.BuildConfiguration
import com.rosahosseini.findr.model.Device
import org.koin.dsl.module

internal val appModule = module {
    factory {
        BuildConfiguration(
            flickerApiKey = BuildConfig.FLICKR_API_KEY,
            isDebug = BuildConfig.DEBUG,
            device = Device.ANDROID // todo
        )
    }
}
