package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.PracticeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PracticeRepository : JpaRepository<PracticeEntity, Int>, PracticeRepositoryCustom {
}