package com.mentalmachines.droidcon_boston.modal

sealed class Result<out R> {
    object Loading : Result<Nothing>()
    class Data<out T>(val data: T) : Result<T>()
    class Error(val message: String) : Result<Nothing>()
}
