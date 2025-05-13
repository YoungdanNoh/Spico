package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.model.*
import com.ssafy.spico.domain.practice.repository.*
import com.ssafy.spico.domain.project.repository.ProjectRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FinalPracticeReportServiceImpl (
    private val projectRepository: ProjectRepository,
    private val practicesRepository: PracticesRepository,
    private val finalReportsRepository: FinalReportsRepository,
    private val questionAnswerRepository: QuestionAnswerRepository,
    private val videoFeedbackPointsRepository: VideoFeedbackPointsRepository,
) : FinalPracticeReportService {

    @Transactional
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

        val feedbackVolumeRecords = videoFeedbackPointsEntity
            ?.filter { it.type.name == "VOLUME" }
            ?.map {
                FeedbackVolumeRecord(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    volumeLevel = it.feedbackDetail
                )
            } ?: emptyList()

        val feedbackSpeedRecords = videoFeedbackPointsEntity
            ?.filter { it.type.name == "SPEED" }
            ?.map {
                FeedbackSpeedRecord(
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
            feedbackVolumeRecords = feedbackVolumeRecords,
            feedbackSpeedRecords = feedbackSpeedRecords,
            pauseRecords = pauseRecords,
            qaRecord = qaRecords

        ).toResponse()
    }

}