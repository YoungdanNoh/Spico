package com.a401.speakoandroid.common.domain

class HandledException(
    userMessage: String,
    cause: Throwable? = null
) : Exception(userMessage, cause)