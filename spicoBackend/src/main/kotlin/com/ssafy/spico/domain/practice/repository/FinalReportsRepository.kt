package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.FinalReportsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FinalReportsRepository : JpaRepository<FinalReportsEntity, Int>, FinalReportsRepositoryCustom {

}