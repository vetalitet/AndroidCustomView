package com.example.customviewsproject

sealed class ApiResult<out T : Any> {

    object Loading : ApiResult<Nothing>()
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception) : ApiResult<Nothing>()

}