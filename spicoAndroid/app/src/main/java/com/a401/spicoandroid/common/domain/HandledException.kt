package com.a401.spicoandroid.common.domain

class HandledException(
    userMessage: String,
    cause: Throwable? = null
) : Exception(userMessage, cause)