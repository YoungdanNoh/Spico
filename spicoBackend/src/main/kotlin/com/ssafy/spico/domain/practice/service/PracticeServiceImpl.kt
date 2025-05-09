package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.gpt.service.GptService
import com.ssafy.spico.domain.practice.dto.EndFinalPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.StartCoachingPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.StartFinalPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.toResponse
import com.ssafy.spico.domain.practice.entity.*
import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.model.*
import com.ssafy.spico.domain.practice.repository.FinalReportRepository
import com.ssafy.spico.domain.practice.repository.PracticeRepository
import com.ssafy.spico.domain.practice.repository.QuestionAnswerRepository
import com.ssafy.spico.domain.project.repository.ProjectRepository
import com.ssafy.spico.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PracticeServiceImpl (
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val practiceRepository: PracticeRepository,
    private val finalReportRepository: FinalReportRepository,
    private val questionAnswerRepository: QuestionAnswerRepository,
    private val gptService: GptService
) : PracticeService {

    @Transactional
    override fun startFinalPractice(
        userId: Int,
        projectId: Int,
        setting: FinalPracticeSetting
    ): StartFinalPracticeResponseDto {

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

    @Transactional
    override fun endFinalPractice(
        userId: Int,
        projectId: Int,
        practiceId: Int,
        speechText: FinalPracticeSpeechText
    ): EndFinalPracticeResponseDto {

        // 1. 해당 practice의 상태를 COMPLETED로 변경
        val practiceEntity = practiceRepository.findById(practiceId)
            .orElseThrow { PracticeException(PracticeError.PRACTICE_NOT_FOUND) }

        practiceEntity.setStatus(PracticeStatus.COMPLETED)

        // 2. final_reports 테이블에 practice_id 넣고, 기존 개수 기반으로 final_practice_cnt 증가
        val lastCnt = finalReportRepository.findLastCntByProject(projectId)
        val finalReportEntity = FinalReportEntity(
            practiceEntity,
            lastCnt + 1,
            speechText.speechContent
        )
        finalReportRepository.save(finalReportEntity)

        //GPT에게 질문 생성 요청
        val userEntity = userRepository.findById(userId)
            .orElseThrow { PracticeException(PracticeError.USER_NOT_FOUND) }

        val questionCount = userEntity.questionCount // 사용자가 설정한 질문 갯수 가져오기
        val questions = gptService.generateQuestions(speechText.speechContent, questionCount)

        // 3. GPT 응답 질문 리스트를 question_answer 테이블에 저장
        val questionAnswerEntities = questions.map { question ->
            QuestionAnswerEntity(
                finalReportEntity,
                question
            )
        }

        questionAnswerRepository.saveAll(questionAnswerEntities)

        return FinalPracticeQuestionList(
            questions = questions
        ).toResponse()
    }

    override fun startCoachingPractice(userId: Int, projectId: Int): StartCoachingPracticeResponseDto {

        val projectEntity = projectRepository.findById(projectId)
            .orElseThrow { PracticeException(PracticeError.PROJECT_NOT_FOUND) } //프로젝트 정보 가져오기

        val practiceEntity = PracticeEntity(
            projectEntity,
            LocalDateTime.now(),
            PracticeType.COACHING,
            PracticeStatus.IN_PROGRESS
        )

        // 연습 테이블에 데이터 생성
        val saved = try {
            practiceRepository.save(practiceEntity)
        } catch (e: Exception) {
            throw PracticeException(PracticeError.PERSISTENCE_ERROR)
        }

        return CoachingPracticeInfo(
            practiceId = saved.practiceId
        ).toResponse()
    }

}