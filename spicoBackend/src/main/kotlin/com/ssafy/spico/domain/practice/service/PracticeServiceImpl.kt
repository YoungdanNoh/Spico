package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.gpt.service.GptService
import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.entity.*
import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.model.*
import com.ssafy.spico.domain.practice.repository.FinalReportsRepository
import com.ssafy.spico.domain.practice.repository.PracticesRepository
import com.ssafy.spico.domain.practice.repository.QuestionAnswerRepository
import com.ssafy.spico.domain.practice.repository.VideoFeedbackPointsRepository
import com.ssafy.spico.domain.project.repository.ProjectRepository
import com.ssafy.spico.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PracticeServiceImpl (
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val practicesRepository: PracticesRepository,
    private val finalReportsRepository: FinalReportsRepository,
    private val questionAnswerRepository: QuestionAnswerRepository,
    private val videoFeedbackPointsRepository: VideoFeedbackPointsRepository,
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

        val practicesEntity = PracticesEntity(
            projectEntity,
            LocalDateTime.now(),
            PracticeType.FINAL,
            PracticeStatus.IN_PROGRESS
        )

        val saved = try {
            practicesRepository.save(practicesEntity)
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
        val practiceEntity = practicesRepository.findById(practiceId)
            .orElseThrow { PracticeException(PracticeError.PRACTICE_NOT_FOUND) }

        practiceEntity.setStatus(PracticeStatus.COMPLETED)

        // 2. final_reports 테이블에 practice_id 넣고, 기존 개수 기반으로 final_practice_cnt 증가
        val lastCnt = finalReportsRepository.findLastCntByProject(projectId)
        val finalReportsEntity = FinalReportsEntity(
            practiceEntity,
            lastCnt + 1,
            speechText.speechContent
        )
        finalReportsRepository.save(finalReportsEntity)

        //GPT에게 질문 생성 요청
        val userEntity = userRepository.findById(userId)
            .orElseThrow { PracticeException(PracticeError.USER_NOT_FOUND) }

        val questionCount = userEntity.questionCount // 사용자가 설정한 질문 갯수 가져오기
        val questions = gptService.generateQuestions(speechText.speechContent, questionCount)

        // 3. GPT 응답 질문 리스트를 question_answer 테이블에 저장
        val questionAnswerEntities = questions.map { question ->
            QuestionAnswerEntity(
                finalReportsEntity,
                question
            )
        }

        questionAnswerRepository.saveAll(questionAnswerEntities)

        return FinalPracticeQuestionList(
            questions = questions
        ).toResponse()
    }

    override fun startCoachingPractice(
        userId: Int,
        projectId: Int
    ): StartCoachingPracticeResponseDto {

        val projectEntity = projectRepository.findById(projectId)
            .orElseThrow { PracticeException(PracticeError.PROJECT_NOT_FOUND) } //프로젝트 정보 가져오기

        val practicesEntity = PracticesEntity(
            projectEntity,
            LocalDateTime.now(),
            PracticeType.COACHING,
            PracticeStatus.IN_PROGRESS
        )

        // 연습 테이블에 데이터 생성
        val saved = try {
            practicesRepository.save(practicesEntity)
        } catch (e: Exception) {
            throw PracticeException(PracticeError.PERSISTENCE_ERROR)
        }

        return CoachingPracticeInfo(
            practiceId = saved.practiceId
        ).toResponse()
    }

    override fun finalPracticeReport(
        userId: Int,
        projectId: Int,
        practiceId: Int
    ): FinalPracticeReportResponseDto {

        // 1. 프로젝트 제목
        val projectEntity = projectRepository.findById(projectId)
            .orElseThrow { PracticeException(PracticeError.PROJECT_NOT_FOUND) }

        // 2. 파이널 모드 제목(finalPracticeCnt + "회차"),
        // videoUrl, totalScore, volumeScore, speedScore, pauseScore, pronunciationScore,
        // scriptMatchRate, speechVolume, speechSpeed, pauseCount 가져오기
        val finalReportsEntity = finalReportsRepository.findReportByPractice(practiceId)
            ?: throw PracticeException(PracticeError.REPORT_NOT_FOUND)

        // 3. 연습 일시 date(createdAt) 가져오기
        val practicesEntity = practicesRepository.findById(
            finalReportsEntity.practicesEntity.practiceId
        ).orElseThrow { PracticeException(PracticeError.PRACTICE_NOT_FOUND) }

        // 4. video_feedback_points 테이블에서
        // volumeRecords, scoreRecords, pauseRecords
        val videoFeedbackPointsEntity = videoFeedbackPointsRepository.findFeedbackByReport(
            finalReportsEntity.finalReportId
        ) ?: throw PracticeException(PracticeError.FEEDBACK_NOT_FOUND)

        val volumeRecords = videoFeedbackPointsEntity
            ?.filter { it.type.name == "VOLUME" }
            ?.map {
                VolumeRecord(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    volumeLevel = it.feedbackDetail
                )
            } ?: emptyList()

        val speedRecords = videoFeedbackPointsEntity
            ?.filter { it.type.name == "SPEED" }
            ?.map {
                SpeedRecord(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    speedLevel = it.feedbackDetail
                )
            } ?: emptyList()

        val pauseRecords = videoFeedbackPointsEntity
            ?.filter { it.type.name == "PAUSE" }
            ?.map {
                PauseRecord(
                    startTime = it.startTime,
                    endTime = it.endTime
                )
            } ?: emptyList()

        // 5. Question_Answer 테이블에서
        // qaRecord 가져오기
        val qaRecords = questionAnswerRepository.findQaByFinal(finalReportsEntity.finalReportId)
            ?.map {
                QaRecord(
                    question = it.question,
                    answer = it.answer
                )
            } ?: emptyList()

        return FinalPracticeReport(
            projectName = projectEntity.title,
            practiceName = "${finalReportsEntity.finalPracticeCnt}회차",
            date = practicesEntity.createdAt,
            videoUrl = finalReportsEntity.videoUrl,
            totalScore = finalReportsEntity.totalScore,
            volumeScore = finalReportsEntity.volumeScore,
            speedScore = finalReportsEntity.speedScore,
            pauseScore = finalReportsEntity.pauseScore,
            pronunciationScore = finalReportsEntity.pronunciationScore,
            scriptMatchRate = finalReportsEntity.scriptMatchRate,
            volumeStatus = finalReportsEntity.speechVolume,
            speedStatus = finalReportsEntity.speechSpeed,
            pauseCount = finalReportsEntity.pauseCount,
            volumeRecords = volumeRecords,
            speedRecords = speedRecords,
            pauseRecords = pauseRecords,
            qaRecord = qaRecords

        ).toResponse()
    }

}