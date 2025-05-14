package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.CoachingReportsEntity

interface CoachingReportsRepositoryCustom {

    fun findReportByPractice(practiceId: Int): CoachingReportsEntity?

}