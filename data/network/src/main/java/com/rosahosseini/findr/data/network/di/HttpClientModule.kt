package com.rosahosseini.findr.data.network.di

import com.rosahosseini.findr.data.network.RemoteErrorManager
import com.rosahosseini.findr.model.BuildConfiguration
import com.rosahosseini.findr.model.Device
import com.rosahosseini.findr.remote.di.FlickrUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class HttpClientModule {

    companion object {
        private const val FLICKR_BASE_URL = "https://api.flickr.com/services/rest/"
    }

    @Provides
    @FlickrUrl
    fun provideBaseUrl(): String = FLICKR_BASE_URL

    @Singleton
    @Provides
    fun providesOkHttpClient(
        engine: HttpClientEngine,
        buildConfiguration: BuildConfiguration,
        remoteErrorManager: RemoteErrorManager
    ): HttpClient {
        return HttpClient(engine) {
            expectSuccess = true
            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, _ ->
                    throw remoteErrorManager.apiError(cause)
                }
            }
            install(Logging) {
                level = if (buildConfiguration.isDebug) LogLevel.ALL else LogLevel.NONE
                logger = if (buildConfiguration.device == Device.ANDROID) {
                    Logger.ANDROID
                } else {
                    Logger.DEFAULT
                }
            }
            install(ContentNegotiation) {
                gson {
                    setLenient()
                    setPrettyPrinting()
                    serializeNulls()
                }
            }
        }
    }
}
