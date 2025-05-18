package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.model.EndCoachingPractice

interface CoachingPracticeService {

    fun startCoachingPractice(
        userId: Int,
        projectId: Int
    ): StartCoachingPracticeResponseDto

    fun endCoachingPractice(
        userId: Int,
        projectId: Int,
        practiceId: Int,
        endCoachingPractice: EndCoachingPractice
    )

}