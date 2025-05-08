package com.ssafy.spico.domain.practice.exception

class PracticeException(val error: PracticeError) : RuntimeException(error.errorMsg)
