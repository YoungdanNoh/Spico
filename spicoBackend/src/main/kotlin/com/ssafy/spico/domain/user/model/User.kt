package com.ssafy.spico.domain.user.model

import com.ssafy.spico.domain.user.entity.UserEntity

data class User(
    val id: Int?,
    val email: String,
    val name: String,
    var hasAudience: Boolean,
    var hasQna: Boolean,
    var questionCount: Int,
    var answerTimeLimit: Int
)

fun UserEntity.toModel(): User {
    return User(
        id = this.id,
        email = this.email,
        name = this.name,
        hasAudience = this.isHasAudience,
        hasQna = this.isHasQna,
        questionCount = this.questionCount,
        answerTimeLimit = this.answerTimeLimit
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        this.email,
        this.name,
        this.hasAudience,
        this.hasQna,
        this.questionCount,
        this.answerTimeLimit
    )
}