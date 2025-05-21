package com.a401.spicoandroid.common.utils

import com.a401.spicoandroid.R

/**
 * 랜덤 스피치 주제에 따른 아이콘 리소스 반환 함수
 */
fun getTopicIconRes(topic: String): Int {
    return when (topic.uppercase()) {
        "BUSINESS" -> R.drawable.img_politics // 실제로 경제인데 정치 아이콘이 저장됨
        "POLITICS" -> R.drawable.img_economy // 실제로 정치인데 경제 아이콘이 저장됨
        "TECHNOLOGY" -> R.drawable.img_it //IT
        "SCIENCE" -> R.drawable.img_science // 과학
        "SPORTS" -> R.drawable.img_sports // 스포츠
        "WORLD" -> R.drawable.img_environment // 세계
        "ENVIRONMENT" -> R.drawable.img_nature // 환경
        "ENTERTAINMENT" -> R.drawable.img_art // 문화예술
        else -> R.drawable.img_list_practice
    }
}

/**
 * 랜덤 스피치 주제의 한글 변환 함수
 */
fun getTopicKor(topic: String): String {
    return when (topic.uppercase()) {
        "BUSINESS" -> "경제"
        "POLITICS" -> "정치"
        "TECHNOLOGY" -> "IT"
        "SCIENCE" -> "과학"
        "SPORTS" -> "스포츠"
        "WORLD" -> "세계"
        "ENVIRONMENT" -> "환경"
        "ENTERTAINMENT" -> "문화·예술"
        else -> "기타"
    }
}