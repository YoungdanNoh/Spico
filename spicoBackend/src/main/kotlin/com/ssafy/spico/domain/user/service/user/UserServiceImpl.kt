package com.ssafy.spico.domain.user.service.user

import com.ssafy.spico.domain.user.dto.FinalSettingsResponse
import com.ssafy.spico.domain.user.dto.toFinalSettingsResponse
import com.ssafy.spico.domain.user.exception.user.UserError
import com.ssafy.spico.domain.user.exception.user.UserException
import com.ssafy.spico.domain.user.model.toModel
import com.ssafy.spico.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun getFinalSettings(userId: Int): FinalSettingsResponse {
        val userEntity = userRepository.findById(userId)
            .orElseThrow { UserException(UserError.USER_NOT_FOUND) }
        println("사용자 정보: "+userEntity.id+", "+userEntity.questionCount)
        return userEntity.toModel().toFinalSettingsResponse()
    }
}