package com.rosahosseini.findr.remote.extensions

import com.rosahosseini.findr.model.ApiError
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.charsets.MalformedInputException
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ensureActive

suspend fun HttpResponse.toResponseException(): ResponseException {
    val exceptionResponse = try {
        bodyAsText()
    } catch (_: MalformedInputException) {
        ""
    }
    return ResponseException(this, exceptionResponse)
}

suspend inline fun <reified T> HttpResponse.toResult(): Result<T> {
    val code = status.value
    return if (code in 200..299) {
        Result.success(this.body<T>())
    } else {
        Result.failure(ApiError(code = code, throwable = this.toResponseException()))
    }
}

suspend inline fun <reified T> catchResult(
    request: () -> HttpResponse
): Result<T> {
    return try {
        request().toResult<T>()
    } catch (e: ApiError) {
        // check if throwable is not from coroutine cancellation
        coroutineContext.ensureActive()
        Result.failure(e)
    }
}
