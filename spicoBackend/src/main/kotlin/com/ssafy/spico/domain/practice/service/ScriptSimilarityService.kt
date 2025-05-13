package com.ssafy.spico.domain.practice.service

interface ScriptSimilarityService {

    fun calculateLevenshteinSimilarity(script: String, sttText: String): Int
}