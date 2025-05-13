package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.model.EndFinalPractice
import com.ssafy.spico.domain.practice.model.FinalPracticeSetting
import com.ssafy.spico.domain.practice.model.FinalPracticeSpeechText

interface FinalPracticeService {

    fun startFinalPractice(
        userId: Int,
        projectId: Int,
        request: FinalPracticeSetting
    ): StartFinalPracticeResponseDto

    fun generateGPTQuestion(
        userId: Int,
        projectId: Int,
        practiceId: Int,
        speechText: FinalPracticeSpeechText
    ): GenerateGPTQuestionResponseDto

    fun endFinalPractice(
        projectId: Int,
        practiceId: Int,
        endFinalPractice: EndFinalPractice
    ): EndFinalPracticeResponseDto

}