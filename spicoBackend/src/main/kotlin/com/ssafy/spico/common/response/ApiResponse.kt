package com.ssafy.spico.common.response

data class ApiResponse<T>(
    var success: Boolean,
    var errorMsg: String? = null,
    var errorCode: String? = null,
    var data: T? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(true, null, null, data)
        }

        fun <T> success(): ApiResponse<T> {
            return ApiResponse(true, null, null, null)
        }

        fun <T> error(errorCode: String, errorMsg: String): ApiResponse<T> {
            return ApiResponse(false, errorMsg, errorCode, null)
        }
    }
}
