package com.ssafy.spico.domain.user.service.user

import com.ssafy.spico.domain.user.dto.FinalSettingsRequest
import com.ssafy.spico.domain.user.dto.FinalSettingsResponse
import com.ssafy.spico.domain.user.dto.toFinalSettingsResponse
import com.ssafy.spico.domain.user.exception.user.UserError
import com.ssafy.spico.domain.user.exception.user.UserException
import com.ssafy.spico.domain.user.model.toModel
import com.ssafy.spico.domain.user.model.toEntity
import com.ssafy.spico.domain.user.model.updateSettings
import com.ssafy.spico.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun getFinalSettings(userId: Int): FinalSettingsResponse {
        val userEntity = userRepository.findById(userId)
            .orElseThrow { UserException(UserError.USER_NOT_FOUND) }
        return userEntity.toModel().toFinalSettingsResponse()
    }

    @Transactional
    override fun updateFinalSettings(userId: Int, request: FinalSettingsRequest): FinalSettingsResponse {

        val userEntity = userRepository.findById(userId)
            .orElseThrow { UserException(UserError.USER_NOT_FOUND) }
        val user = userEntity.toModel()

        return userRepository
            .save(user.updateSettings(request).toEntity())
            .toModel().toFinalSettingsResponse()

    }

}