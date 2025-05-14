package com.ssafy.spico.domain.landingPage.repository

import com.ssafy.spico.domain.practice.entity.PracticesEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LandingPageReportRepository : JpaRepository<PracticesEntity, Int>, LandingPageReportRepositoryCustom {
}