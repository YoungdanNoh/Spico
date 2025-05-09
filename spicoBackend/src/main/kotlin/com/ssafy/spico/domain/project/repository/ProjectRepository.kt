package com.ssafy.spico.domain.project.repository

import com.ssafy.spico.domain.project.entity.ProjectEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository : JpaRepository<ProjectEntity, Int>, ProjectRepositoryCustom {
}