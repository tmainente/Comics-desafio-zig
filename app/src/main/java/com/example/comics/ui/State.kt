package com.example.comics.ui

sealed class State<T>(
    val data: T? = null,
    val message: String? = null
) {
    class ErrorOffline<T>(message: String, data: T? = null) : State<T>(data, message)
    class Success<T>(data: T) : State<T>(data)
    class Loading<T>(data: T? = null) : State<T>(data)
    class Error<T>(message: String, data: T? = null) : State<T>(data, message)
}