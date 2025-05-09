package com.ssafy.spico.domain.project.service

import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.exception.ProjectError
import com.ssafy.spico.domain.project.exception.ProjectException
import com.ssafy.spico.domain.project.model.Project
import com.ssafy.spico.domain.project.model.toEntity
import com.ssafy.spico.domain.project.model.toModel
import com.ssafy.spico.domain.project.repository.ProjectRepository
import com.ssafy.spico.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
): ProjectService {
    override fun getProjects(cursor: Int?, size: Int, type: ProjectViewType): List<Project> {

        require(size >= 1) { throw ProjectException(ProjectError.INVALID_PAGE_SIZE) }

        val entities = projectRepository.findProjectWithPaging(cursor, size, type)
        return entities.map { it.toModel() }
    }

    override fun createProject(project: Project): Int {
        val userEntity = userRepository.findById(project.userId)
            .orElseThrow { ProjectException(ProjectError.USER_NOT_FOUND) }
        val projectEntity = project.toEntity(userEntity)
        val saved = try {
            projectRepository.save(projectEntity)
        } catch (e: Exception) {
            throw ProjectException(ProjectError.PERSISTENCE_ERROR)
        }
        return saved.projectId
    }
}