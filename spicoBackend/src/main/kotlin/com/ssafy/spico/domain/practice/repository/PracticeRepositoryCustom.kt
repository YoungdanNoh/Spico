package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.PracticeEntity

interface PracticeRepositoryCustom {
    fun findFinalPracticeByProjectId(projectId: Int): List<PracticeEntity>
}