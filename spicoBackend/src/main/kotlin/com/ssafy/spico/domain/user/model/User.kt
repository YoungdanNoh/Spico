package com.ssafy.spico.domain.user.model

import com.ssafy.spico.domain.user.entity.UserEntity

data class User(
    val id: Int,
    val kakaoId: Long,
    val nickname: String,
    var hasAudience: Boolean,
    var hasQna: Boolean,
    var questionCount: Int,
    var answerTimeLimit: Int
)

fun UserEntity.toModel(): User {
    return User(
        id = this.id,
        kakaoId = this.kakaoId,
        nickname = this.nickname,
        hasAudience = this.isHasAudience,
        hasQna = this.isHasQna,
        questionCount = this.questionCount,
        answerTimeLimit = this.answerTimeLimit
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        this.kakaoId,
        this.nickname,
        this.hasAudience,
        this.hasQna,
        this.questionCount,
        this.answerTimeLimit
    )
}