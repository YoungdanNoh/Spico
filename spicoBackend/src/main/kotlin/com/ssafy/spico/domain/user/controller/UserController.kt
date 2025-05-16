package com.ssafy.spico.domain.user.controller

import com.ssafy.spico.common.annotaion.UserId
import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.user.dto.FinalSettingsRequest
import com.ssafy.spico.domain.user.dto.FinalSettingsResponse
import com.ssafy.spico.domain.user.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me/final-settings")
    fun getFinalModeSettings(
        @UserId userId: Int
    ): ApiResponse<FinalSettingsResponse>{
        val response = userService.getFinalSettings(userId)
        return ApiResponse.success(response)
    }

    @PatchMapping("/me/final-settings")
    fun patchFinalModeSettings(
        @UserId userId: Int,
        @RequestBody request: FinalSettingsRequest
    ): ApiResponse<FinalSettingsResponse> {
        val response = userService.updateFinalSettings(userId, request)
        return ApiResponse.success(response)
    }

}