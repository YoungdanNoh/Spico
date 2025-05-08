package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.PracticeEntity

interface FinalReportRepositoryCustom {
    fun findLastCntByProject(projectId: Int): Int
}