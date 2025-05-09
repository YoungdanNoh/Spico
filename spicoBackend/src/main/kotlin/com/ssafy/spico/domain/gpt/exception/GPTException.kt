package com.ssafy.spico.domain.practice.exception

class GPTException(val error: GPTError) : RuntimeException(error.errorMsg)
