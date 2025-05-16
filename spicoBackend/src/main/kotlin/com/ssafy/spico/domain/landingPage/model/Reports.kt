package com.ssafy.spico.domain.landingPage.model

import com.ssafy.spico.domain.practice.entity.PracticeType

data class Reports(
    val type: PracticeType?,
    val projectId: Int?,
    val practiceId: Int?,
    val projectName: String?,
    val practiceName: String?,
    val reportsId: Int?

)
