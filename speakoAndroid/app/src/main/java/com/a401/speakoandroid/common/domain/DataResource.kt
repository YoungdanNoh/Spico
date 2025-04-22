package com.a401.speakoandroid.common.domain

sealed interface DataResource<out T> {
    data class Success<out T>(val data: T) : DataResource<T>
    data class Error(val throwable: Throwable) : DataResource<Nothing>
    data class Loading<out T>(val data: T? = null) : DataResource<T>

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(throwable: Throwable) = Error(throwable)
        fun <T> loading(data: T? = null) : Loading<T> = Loading(data)
    }
}