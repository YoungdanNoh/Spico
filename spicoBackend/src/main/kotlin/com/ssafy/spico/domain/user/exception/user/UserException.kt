package com.ssafy.spico.domain.user.exception.user

class UserException(val error: UserError) : RuntimeException(error.errorMsg){
}