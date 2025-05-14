package com.ssafy.spico.domain.user.repository

import com.ssafy.spico.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Int> {
    fun findByKakaoId(kakaoId: Long): UserEntity?
}