package com.ssafy.spico.domain.user.service.user

import com.ssafy.spico.domain.user.dto.FinalSettingsRequest
import com.ssafy.spico.domain.user.dto.FinalSettingsResponse

interface UserService {
    fun getFinalSettings(userId: Int): FinalSettingsResponse
    fun updateFinalSettings(userId: Int, request: FinalSettingsRequest): FinalSettingsResponse
}