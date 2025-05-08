package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.practice.dto.FinalPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.toResponse
import com.ssafy.spico.domain.practice.entity.PracticeEntity
import com.ssafy.spico.domain.practice.entity.PracticeStatus
import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.model.FinalPracticeInfo
import com.ssafy.spico.domain.practice.model.FinalPracticeSetting
import com.ssafy.spico.domain.practice.repository.PracticeRepository
import com.ssafy.spico.domain.project.repository.ProjectRepository
import com.ssafy.spico.domain.user.model.toEntity
import com.ssafy.spico.domain.user.model.toModel
import com.ssafy.spico.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PracticeServiceImpl (
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val practiceRepository: PracticeRepository
) : PracticeService {

    @Transactional
    override fun createFinalPractice(
        projectId: Int,
        userId: Int,
        setting: FinalPracticeSetting
    ): FinalPracticeResponseDto {

        val projectEntity = projectRepository.findById(projectId)
            .orElseThrow { PracticeException(PracticeError.PROJECT_NOT_FOUND) }

        val userEntity = userRepository.findById(userId)
            .orElseThrow { PracticeException(PracticeError.USER_NOT_FOUND) }

        userEntity.updateSetting(
            setting.hasAudience,
            setting.hasQnA,
            setting.questionCount,
            setting.answerTimeLimit
        )

        val practiceEntity = PracticeEntity(
            projectEntity,
            LocalDateTime.now(),
            PracticeType.FINAL,
            PracticeStatus.IN_PROGRESS
        )

        val saved = try {
            practiceRepository.save(practiceEntity)
        } catch (e: Exception) {
            throw PracticeException(PracticeError.PERSISTENCE_ERROR)
        }
        return FinalPracticeInfo(
            practiceId = saved.practiceId
        ).toResponse()
    }
}