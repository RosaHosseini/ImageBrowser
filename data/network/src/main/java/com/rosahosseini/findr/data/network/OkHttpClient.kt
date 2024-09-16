package com.rosahosseini.findr.data.network

import com.rosahosseini.findr.model.BuildConfiguration
import com.rosahosseini.findr.model.Device
import com.rosahosseini.findr.remote.extensions.ErrorManager
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

fun configureOkHttpClient(
    engine: HttpClientEngine,
    buildConfiguration: BuildConfiguration,
    errorManager: ErrorManager
): HttpClient {
    return HttpClient(engine) {
        expectSuccess = true
        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, _ ->
                throw errorManager.apiError(cause)
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
