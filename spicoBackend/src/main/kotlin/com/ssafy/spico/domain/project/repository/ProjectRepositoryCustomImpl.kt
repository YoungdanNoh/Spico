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

    override fun findProjectsWithPaging(userId: Int, cursor: Int?, size: Int, type: ProjectViewType): List<ProjectEntity> {
        val project = QProjectEntity.projectEntity

        val today = LocalDate.now()

        val cursorCondition = when (type) {
            ProjectViewType.HOME -> {
                val dateCondition = project.date.goe(today)
                if (cursor != null) dateCondition.and(project.projectId.gt(cursor)) else dateCondition
            }
            ProjectViewType.LIST -> {
                if (cursor != null) project.projectId.lt(cursor) else null
            }
            ProjectViewType.CAL -> {
                if (cursor != null) project.projectId.gt(cursor) else null
            }
        }

        val userCondition = project.userEntity.id.eq(userId)

        val whereCondition = if (cursorCondition != null) {
            userCondition.and(cursorCondition)
        } else {
            userCondition
        }

        val baseQuery = queryFactory
            .selectFrom(project)
            .where(whereCondition)
            .limit(size.toLong())
            .orderBy(
                when (type) {
                    ProjectViewType.HOME, ProjectViewType.CAL -> project.date.asc()
                    ProjectViewType.LIST -> project.createdAt.desc()
                }
            )
        return baseQuery.fetch()
    }
}