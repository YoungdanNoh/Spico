package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.gpt.service.GptService
import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.entity.*
import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.model.*
import com.ssafy.spico.domain.practice.repository.*
import com.ssafy.spico.domain.project.repository.ProjectRepository
import com.ssafy.spico.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Service
class FinalPracticeServiceImpl (
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val practicesRepository: PracticesRepository,
    private val finalReportsRepository: FinalReportsRepository,
    private val questionAnswerRepository: QuestionAnswerRepository,
    private val videoFeedbackPointsRepository: VideoFeedbackPointsRepository,
    private val gptService: GptService,
    private val minioService: MinioService,
    private val scriptSimilarityService: ScriptSimilarityService
) : FinalPracticeService {

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

        val practiceEntity = PracticesEntity(
            projectEntity,
            LocalDateTime.now(),
            PracticeType.FINAL,
            PracticeStatus.IN_PROGRESS
        )

        val saved = try {
            practicesRepository.save(practiceEntity)
        } catch (e: Exception) {
            throw PracticeException(PracticeError.PERSISTENCE_ERROR)
        }

        return FinalPracticeInfo(
            practiceId = saved.practiceId
        ).toResponse()
    }

    @Transactional
    override fun generateGPTQuestion(
        userId: Int,
        projectId: Int,
        practiceId: Int,
        speechText: FinalPracticeSpeechText
    ): GenerateGPTQuestionResponseDto {

        // 1. 해당 practice의 상태를 COMPLETED로 변경
        // 이미 완료된 상태라면 예외 발생
        val practiceEntity = practicesRepository.findById(practiceId)
            .orElseThrow { PracticeException(PracticeError.PRACTICE_NOT_FOUND) }

        if (practiceEntity.status == PracticeStatus.COMPLETED) {
            throw PracticeException(PracticeError.ALREADY_COMPLETED_PRACTICE)
        }

        practiceEntity.setStatus(PracticeStatus.COMPLETED)

        // 2. final_reports 테이블에 practice_id 넣고, 기존 개수 기반으로 final_practice_cnt 증가
        //val lastCnt = finalReportsRepository.findLastCntByProject(projectId)
        val projectEntity = projectRepository.findById(projectId)
            .orElseThrow { PracticeException(PracticeError.PROJECT_NOT_FOUND) }
        projectEntity.lastFinalCnt += 1

        val finalReportsEntity = FinalReportsEntity(
            practiceEntity,
            projectEntity.lastFinalCnt,
            speechText.speechContent
        )
        finalReportsRepository.save(finalReportsEntity)

        //GPT에게 질문 생성 요청
        val userEntity = userRepository.findById(userId)
            .orElseThrow { PracticeException(PracticeError.USER_NOT_FOUND) }

        val questionCount = userEntity.questionCount // 사용자가 설정한 질문 갯수 가져오기
        val questions = gptService.generateQuestions(speechText.speechContent, questionCount)

        // 3. GPT 응답 질문 리스트를 question_answer 테이블에 저장
        val savedQuestionEntities = questionAnswerRepository.saveAll(
            questions.map{ q ->
                QuestionAnswerEntity(
                    finalReportsEntity,
                    q
                )
            }
        )

        // 4. 저장된 question의 id로 응답 모델 구성
        val questionModel = savedQuestionEntities.map {
            Question(
                questionId = it.questionAnswerId,
                question = it.question
            )
        }

        return FinalPracticeQuestionList(
            questions = questionModel
        ).toResponse()
    }

    @Transactional
    override fun endFinalPractice(
        projectId: Int,
        practiceId: Int,
        endFinalPractice: EndFinalPractice
    ): EndFinalPracticeResponseDto {

        val practiceEntity = practicesRepository.findById(practiceId)
            .orElseThrow { PracticeException(PracticeError.PRACTICE_NOT_FOUND) }

        // 1. qaRecords가 비어있으면
        //    finalReportsEntity 의 final_practice_cnt 값을 1개 증가시키기
        //    projectsEntity 의 last_final_cnt 값을 1 증가시키기
        println("endFinalPractice.answers${endFinalPractice.answers}")
        if(endFinalPractice.answers.isNullOrEmpty()){
            // 1-1. 해당 practice의 상태를 COMPLETED로 변경
            // 이미 완료된 프로젝트 상태라면 예외 발생
            if (practiceEntity.status == PracticeStatus.COMPLETED) {
                throw PracticeException(PracticeError.ALREADY_COMPLETED_PRACTICE)
            }

            practiceEntity.setStatus(PracticeStatus.COMPLETED)

            // 1-2. final_reports 테이블에 practice_id 넣고, 기존 개수 기반으로 final_practice_cnt 증가
            val projectEntity = projectRepository.findById(projectId)
                .orElseThrow { PracticeException(PracticeError.PROJECT_NOT_FOUND) }
            projectEntity.lastFinalCnt += 1

            val existingReport = finalReportsRepository.findReportByPractice(practiceId)
            if (existingReport != null) {
                throw PracticeException(PracticeError.DUPLICATED_FINAL_REPORT)
            }

            val finalReportsEntity = FinalReportsEntity(
                practiceEntity,
                projectEntity.lastFinalCnt,
                endFinalPractice.speechContent
            )
            finalReportsRepository.save(finalReportsEntity)
        }

        // 2. Video_Feedback_Points에 데이터 넣기
        val finalReportsEntity = finalReportsRepository.findReportByPractice(practiceId)
            ?: throw PracticeException(PracticeError.REPORT_NOT_FOUND)

        val feedbackEntities = mutableListOf<VideoFeedbackPointsEntity>()

        // Volume
        endFinalPractice.volumeRecords?.forEach { record ->
            feedbackEntities.add(
                VideoFeedbackPointsEntity(
                    finalReportsEntity,
                    FeedbackType.VOLUME,
                    record.startTime,
                    record.endTime,
                    record.volumeLevel // QUIET, LOUD 등
                )
            )
        }

        // Speed
        endFinalPractice.speedRecords?.forEach { record ->
            feedbackEntities.add(
                VideoFeedbackPointsEntity(
                    finalReportsEntity,
                    FeedbackType.SPEED,
                    record.startTime,
                    record.endTime,
                    record.speedLevel // SLOW, FAST 등
                )
            )
        }

        // Pause
        endFinalPractice.pauseRecords?.forEach { record ->
            feedbackEntities.add(
                VideoFeedbackPointsEntity(
                    finalReportsEntity,
                    FeedbackType.PAUSE,
                    record.startTime,
                    record.endTime,
                    null // 정지 상태는 세부 타입 없음
                )
            )
        }

        videoFeedbackPointsRepository.saveAll(feedbackEntities)

        // 3. GPT 질문에 대한 답변 저장하기
        endFinalPractice.answers?.forEach { answer ->
            val questionEntity = questionAnswerRepository.findById(answer.questionId)
                .orElseThrow { PracticeException(PracticeError.QUESTION_NOT_FOUND) }

            // 보안: 현재 finalReportId와 동일한 질문인지 검증
            if (questionEntity.finalReportsEntity.finalReportId != finalReportsEntity.finalReportId) {
                throw PracticeException(PracticeError.UNAUTHORIZED_QUESTION_ACCESS)
            }

            questionEntity.setAnswer(answer.answer)
        }

        // 4. Presigned Url 생성하기
        if(endFinalPractice.fileName.isNullOrBlank()) {
            throw PracticeException(PracticeError.INVALID_FILENAME)
        }

        if (endFinalPractice.pronunciationScore != null && endFinalPractice.pronunciationScore !in 0..100) {
            throw PracticeException(PracticeError.INVALID_PRONUNCIATION_SCORE)
        }

        val presignedUrl = minioService.generatePresignedUrl("video", endFinalPractice.fileName)
        val playbackUrl = "video/${endFinalPractice.fileName}"

        finalReportsEntity.videoUrl = playbackUrl

        // 5. FinalReports에 데이터 저장하기
        finalReportsEntity.pronunciationScore = endFinalPractice.pronunciationScore
        finalReportsEntity.pauseScore = endFinalPractice.pauseScore
        finalReportsEntity.speedScore = endFinalPractice.speedScore
        finalReportsEntity.volumeScore = endFinalPractice.volumeScore
        finalReportsEntity.pauseCount = endFinalPractice.pauseCount
        finalReportsEntity.speechSpeed = endFinalPractice.speedStatus
        finalReportsEntity.speechVolume = endFinalPractice.volumeStatus

        // 대본 일치율 계산
        val matchRate = scriptSimilarityService.calculateLevenshteinSimilarity(
            finalReportsEntity.script ?: "",
            endFinalPractice.speechContent ?: ""
        )
        println("scriptMatchRate: $matchRate for practiceId=$practiceId")
        finalReportsEntity.scriptMatchRate = matchRate


        // 평균 점수 계산
        val scoreList = listOfNotNull(
            endFinalPractice.pronunciationScore,
            endFinalPractice.pauseScore,
            endFinalPractice.speedScore,
            endFinalPractice.volumeScore
        )

        val totalScore = if (scoreList.isNotEmpty()) {
            (scoreList.sum().toDouble() / scoreList.size).roundToInt()
        } else null

        finalReportsEntity.totalScore = totalScore

        return PresignedUrl(
            presignedUrl = presignedUrl
        ).toEndFinalResponse()

    }

}