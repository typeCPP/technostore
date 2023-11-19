package com.technostore.arch.result


sealed class Result<out T : Any?> {
    class Success<out T : Any?>(val data: T? = null) : Result<T>()
    class Error<out T : Any?>(val error: ErrorType? = null) : Result<T>()
}