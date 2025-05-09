package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.FinalReportsEntity

interface FinalReportsRepositoryCustom {

    fun findLastCntByProject(projectId: Int): Int

    fun findReportByPractice(practiceId: Int): FinalReportsEntity?

}