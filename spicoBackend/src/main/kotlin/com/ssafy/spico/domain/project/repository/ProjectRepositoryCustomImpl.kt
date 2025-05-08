package com.ssafy.spico.domain.project.repository

import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.model.Project
import org.springframework.stereotype.Repository

@Repository
class ProjectRepositoryCustomImpl(

) : ProjectRepositoryCustom {

    override fun findProjectWithPaging(cursor: Long?, size: Int, type: ProjectViewType): List<Project> {
        TODO("Not yet implemented")
    }

}