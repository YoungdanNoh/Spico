package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.practice.dto.FinalPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.toResponse
import com.ssafy.spico.domain.practice.entity.PracticeEntity
import com.ssafy.spico.domain.practice.entity.PracticeStatus
import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.model.FinalPracticeInfo
import com.ssafy.spico.domain.practice.model.FinalPracticeSetting
import com.ssafy.spico.domain.practice.repository.PracticeRepository
import com.ssafy.spico.domain.project.repository.ProjectRepository
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
    ): ApiResponse<FinalPracticeResponseDto> {

        val projectEntity = projectRepository.findById(projectId)
            .orElseThrow { IllegalArgumentException("프로젝트를 찾을 수 없습니다.") }

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다.") }

        // 사용자의 파이널 모드 설정 값 업데이트
        user.updateHasAudience(setting.hasAudience)
        user.updateHasQna(setting.hasQnA)
        user.updateQuestionCount(setting.questionCount)
        user.updateAnswerTimeLimit(setting.answerTimeLimit)

        val practiceEntity = PracticeEntity(
            projectEntity,
            LocalDateTime.now(),
            PracticeType.FINAL,
            PracticeStatus.IN_PROGRESS
        )

        val saved = practiceRepository.save(practiceEntity)

        // 추후 hasQnA 처리 가능 (QuestionAnswer 생성 등)
        return ApiResponse.success(FinalPracticeInfo(
            practiceId = saved.practiceId
        ).toResponse())
    }
}