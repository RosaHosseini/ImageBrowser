package com.rosahosseini.findr.data.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

val httpEngineModule = module {
    single<HttpClientEngine> { OkHttp.create() }
}
