package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.EndFinalPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.StartFinalPracticeResponseDto
import com.ssafy.spico.domain.practice.model.FinalPracticeSetting
import com.ssafy.spico.domain.practice.model.FinalPracticeSpeechText

interface PracticeService {

    fun createFinalPractice(
        userId: Int,
        projectId: Int,
        request: FinalPracticeSetting
    ): StartFinalPracticeResponseDto

    fun endFinalPractice(
        userId: Int,
        projectId: Int,
        practiceId: Int,
        speechText: FinalPracticeSpeechText
    ): EndFinalPracticeResponseDto
}