package com.ssafy.spico.domain.practice.service

import org.springframework.stereotype.Service
import kotlin.math.max

@Service
class ScriptSimilarityServiceImpl : ScriptSimilarityService {

    override fun calculateLevenshteinSimilarity(script: String, sttText: String): Int {
        val words1 = script.trim().split("\\s+".toRegex())
        val words2 = sttText.trim().split("\\s+".toRegex())

        // 하나라도 비어있으면 비교 불가 → 유사도 0점
        if (words1.isEmpty() || words2.isEmpty()) return 0

        val n = words1.size
        val m = words2.size

        val dp = Array(n + 1) { IntArray(m + 1) }

        for (i in 0..n) dp[i][0] = i
        for (j in 0..m) dp[0][j] = j

        for (i in 1..n) {
            for (j in 1..m) {
                dp[i][j] = if (words1[i - 1] == words2[j - 1]) {
                    dp[i - 1][j - 1]
                } else {
                    minOf(
                        dp[i - 1][j] + 1,    // 삭제
                        dp[i][j - 1] + 1,    // 삽입
                        dp[i - 1][j - 1] + 1 // 교체
                    )
                }
            }
        }

        val levenshteinDistance = dp[n][m]
        val maxLen = max(n, m)
        return (((maxLen - levenshteinDistance).toDouble() / maxLen) * 100).toInt()
    }

}