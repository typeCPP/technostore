package com.technostore.arch.result


sealed class Result<out T : Any?> {
    data class Success<out T : Any?>(val data: T? = null) : Result<T>()
    data class Error<out T : Any?>(val error: ErrorType? = null) : Result<T>()
}