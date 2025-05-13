package com.a401.spicoandroid.common.utils

/**
 * 초 단위를 "X분 XX초" 형식으로 변환
 * 예: 90 → "1분 30초"
 */
fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60

    return if (min == 0) {
        "${sec}초"
    } else {
        "${min}분 ${sec.toString().padStart(2, '0')}초"
    }
}

/**
 * 분과 초를 하나의 초 단위 정수로 변환
 * 예: 1분 30초 → 90
 */
fun toTotalSeconds(minute: Int, second: Int): Int {
    return minute * 60 + second
}

/**
 * 총 초 단위를 분과 초로 나누어 Pair로 반환
 * 예: 90 → Pair(1, 30)
 */
fun fromTotalSeconds(totalSeconds: Int): Pair<Int, Int> {
    return Pair(totalSeconds / 60, totalSeconds % 60)
}
