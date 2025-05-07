package com.ssafy.spico.domain.practice.exception

class SearchException(val error: SearchError) : RuntimeException(error.errorMsg)
