package com.nikitamaslov.textlytic.util

sealed class Response<out T> {

    data class Success<out T>(val content: T) : Response<T>()
    data class Failure(val cause: Throwable? = null) : Response<Nothing>()
}