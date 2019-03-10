package com.mentalmachines.droidcon_boston.modal

sealed class Result<T> {
    class Loading<T> : Result<T>()
    class Data<T>(val data: T) : Result<T>()
    class Error<T>(val message: String) : Result<T>()
}