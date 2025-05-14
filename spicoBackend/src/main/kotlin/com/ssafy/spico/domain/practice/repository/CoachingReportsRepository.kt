package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.CoachingReportsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CoachingReportsRepository : JpaRepository<CoachingReportsEntity, Int>, CoachingReportsRepositoryCustom {

}