package com.morfitimelocal.application

sealed class State<out T> {
    class Loading<out T> : State<T>()
    data class Success<out T>(val data: T) : State<T>()
    data class Failure(val exception: Exception) : State<Nothing>()
}

inline fun <T, R> State<T>.mapSuccess(mapper: (T) -> R) = when(this) {
    is State.Success -> mapper(data) as Any
    else -> this
}