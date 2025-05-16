package com.ssafy.spico.domain.practice.entity

enum class PracticeType {
    COACHING,
    FINAL;

    fun toKorean(): String {
        return when (this) {
            COACHING -> "코칭"
            FINAL -> "파이널"
        }
    }
}