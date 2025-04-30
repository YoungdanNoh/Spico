package com.a401.spicoandroid.common.utils

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.domain.ExceptionHandler

suspend fun <T> safeApiCall(
    block: suspend () -> T
): DataResource<T> {
    return try {
        DataResource.success(block())
    } catch (e: Throwable) {
        DataResource.error(ExceptionHandler.handle(e))
    }
}