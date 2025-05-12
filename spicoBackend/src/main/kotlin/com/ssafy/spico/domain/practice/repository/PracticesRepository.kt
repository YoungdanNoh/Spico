package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.PracticesEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PracticesRepository : JpaRepository<PracticesEntity, Int> {
}