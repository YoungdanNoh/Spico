package com.ssafy.spico.domain.practice.entity

enum class PracticeType(val value: String) {
    COACHING("코칭"),
    FINAL("파이널");

    companion object {
        fun fromValue(name: String): PracticeType? {
            return entries.find { it.value == name }
        }
    }
}