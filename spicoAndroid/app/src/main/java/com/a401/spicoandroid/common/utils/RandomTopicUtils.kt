package com.a401.spicoandroid.common.utils

import com.a401.spicoandroid.R

/**
 * 랜덤 스피치 주제에 따른 아이콘 리소스 반환 함수
 */
fun getTopicIconRes(topic: String): Int {
    return when (topic.lowercase()) {
        "politics" -> R.drawable.img_politics
        "economy" -> R.drawable.img_economy
        "it" -> R.drawable.img_it
        "sports" -> R.drawable.img_sports
        "nature" -> R.drawable.img_nature
        "culture" -> R.drawable.img_culture
        "society" -> R.drawable.img_society
        "science" -> R.drawable.img_science
        "art" -> R.drawable.img_art
        "health" -> R.drawable.img_health
        "history" -> R.drawable.img_history
        "environment" -> R.drawable.img_environment
        else -> R.drawable.img_list_practice // 기본 아이콘
    }
}

/**
 * 랜덤 스피치 주제의 한글 변환 함수
 */
fun getTopicKor(topic: String): String {
    return when (topic.lowercase()) {
        "politics" -> "정치"
        "economy" -> "경제"
        "society" -> "사회"
        "culture" -> "문화"
        "it" -> "IT"
        "science" -> "과학"
        "history" -> "역사"
        "environment" -> "환경"
        "sports" -> "스포츠"
        "health" -> "건강"
        "art" -> "예술"
        "nature" -> "자연"
        else -> "기타"
    }
}