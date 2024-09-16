package com.rosahosseini.findr.data.network

import com.rosahosseini.findr.model.ApiError
import com.rosahosseini.findr.remote.extensions.ErrorManager
import io.ktor.client.plugins.ResponseException

class RemoteErrorManager : ErrorManager {

    override fun apiError(cause: Throwable): ApiError {
        if (cause is ApiError) return cause
        return ApiError(
            code = errorCode(cause),
            throwable = cause
        )
    }

    private fun errorCode(cause: Throwable): Int? {
        return (cause as? ResponseException)?.response?.status?.value
    }
}
