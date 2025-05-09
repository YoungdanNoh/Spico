package com.ssafy.spico.domain.project.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.entity.ProjectEntity
import com.ssafy.spico.domain.project.entity.QProjectEntity
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ProjectRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ProjectRepositoryCustom {

    override fun findProjectWithPaging(cursor: Int?, size: Int, type: ProjectViewType): List<ProjectEntity> {
        val project = QProjectEntity.projectEntity

        val baseQuery = queryFactory
            .selectFrom(project)
            .limit(size.toLong())
            .where(
                when(type) {
                    ProjectViewType.HOME -> {
                        val today = LocalDate.now()
                        val condition = project.date.goe(today)
                        if(cursor != null) condition.and(project.projectId.gt(cursor)) else condition
                    }
                    ProjectViewType.LIST -> {
                        if (cursor != null) project.projectId.lt(cursor) else null
                    }
                    ProjectViewType.CAL -> {
                        if (cursor != null) project.projectId.gt(cursor) else null
                    }
                }
            )
            .orderBy(
                when (type) {
                    ProjectViewType.HOME, ProjectViewType.CAL -> project.date.asc()
                    ProjectViewType.LIST -> project.createdAt.desc()
                }
            )
        return baseQuery.fetch()
    }
}