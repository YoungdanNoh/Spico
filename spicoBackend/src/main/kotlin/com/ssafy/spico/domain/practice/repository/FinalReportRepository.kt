package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.FinalReportEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FinalReportRepository : JpaRepository<FinalReportEntity, Int>, FinalReportRepositoryCustom {
}