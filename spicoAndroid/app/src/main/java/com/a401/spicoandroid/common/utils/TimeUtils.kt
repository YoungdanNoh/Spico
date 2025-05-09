package com.a401.spicoandroid.common.utils

/**
 * 초 단위를 "X분 XX초"로 포맷
 * 예: 90 → "1분 30초"
 */
fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60

    val result = if (min == 0) {
        "${sec}초"
    } else {
        "${min}분 ${sec.toString().padStart(2, '0')}초"
    }
    return result
}

