package com.ssafy.spico.domain.landingPage.repository

import com.ssafy.spico.domain.practice.entity.PracticesEntity

interface LandingPageReportRepositoryCustom {

    fun findTop3ByUserIdOrderByCreatedAtDesc(userId: Int): List<PracticesEntity>
}