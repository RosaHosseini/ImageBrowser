package com.rosahosseini.findr.remote.extensions

import com.rosahosseini.findr.model.ApiError

fun interface ErrorManager {
    fun apiError(cause: Throwable): ApiError
}
