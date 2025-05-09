package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.EndFinalPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.FinalPracticeReportResponseDto
import com.ssafy.spico.domain.practice.dto.StartCoachingPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.StartFinalPracticeResponseDto
import com.ssafy.spico.domain.practice.model.FinalPracticeSetting
import com.ssafy.spico.domain.practice.model.FinalPracticeSpeechText

interface PracticeService {

    fun startFinalPractice(
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

    fun startCoachingPractice(
        userId: Int,
        projectId: Int
    ): StartCoachingPracticeResponseDto

    fun finalPracticeReport(
        userId: Int,
        projectId: Int,
        practiceId: Int
    ): FinalPracticeReportResponseDto

}