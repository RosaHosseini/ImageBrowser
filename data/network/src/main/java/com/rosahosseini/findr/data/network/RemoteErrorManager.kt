package com.rosahosseini.findr.data.network

import com.rosahosseini.findr.model.ApiError
import com.rosahosseini.findr.remote.extensions.ErrorManager
import io.ktor.client.plugins.ResponseException
import javax.inject.Inject

class RemoteErrorManager @Inject constructor() : ErrorManager {

    override fun apiError(cause: Throwable): ApiError {
        return ApiError(
            code = errorCode(cause),
            throwable = cause
        )
    }

    private fun errorCode(cause: Throwable): Int? {
        return (cause as? ResponseException)?.response?.status?.value
    }
}
