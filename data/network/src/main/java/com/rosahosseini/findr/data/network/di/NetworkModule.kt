package com.rosahosseini.findr.data.network.di

import com.rosahosseini.findr.data.network.RemoteErrorManager
import com.rosahosseini.findr.data.network.configureOkHttpClient
import com.rosahosseini.findr.di.flickerUrlQualifier
import com.rosahosseini.findr.remote.extensions.ErrorManager
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val FLICKR_BASE_URL = "https://api.flickr.com/services/rest/"

val networkModule = module {
    factoryOf(::RemoteErrorManager) bind ErrorManager::class
    single { configureOkHttpClient(get(), get(), get()) }
    factory(flickerUrlQualifier) { FLICKR_BASE_URL }
}
