package com.rosahosseini.findr.remote.extensions

import com.rosahosseini.findr.model.ApiError
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpResponse.toResult(): Result<T> {
    val code = status.value
    return if (code in 200..299) {
        Result.success(this.body<T>())
    } else {
        Result.failure(
            ApiError(
                code = code,
                throwable = ResponseException(this, "")
            )
        )
    }
}

suspend inline fun <reified T> catchResult(
    request: () -> HttpResponse
): Result<T> {
    return runCatching {
        request()
    }.fold(
        onSuccess = { it.toResult() },
        onFailure = { Result.failure(it) }
    )
}
