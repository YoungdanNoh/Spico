package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.model.CoachingPracticeReport
import com.ssafy.spico.domain.practice.repository.CoachingReportsRepository
import com.ssafy.spico.domain.practice.repository.FinalReportsRepository
import com.ssafy.spico.domain.practice.repository.PracticesRepository
import com.ssafy.spico.domain.project.repository.ProjectRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CoachingPracticeReportServiceImpl(
    private val projectRepository: ProjectRepository,
    private val practicesRepository: PracticesRepository,
    private val coachingReportsRepository: CoachingReportsRepository,
    private val minioService: MinioService,
) : CoachingPracticeReportService {

    @Transactional
    override fun coachingPracticeReport(projectId: Int, practiceId: Int): CoachingPracticeReportResponseDto {

        // 1. 프로젝트 제목. projectEntity.title
        val projectEntity = projectRepository.findById(projectId)
            .orElseThrow { PracticeException(PracticeError.PROJECT_NOT_FOUND) }

        // 2. 코칭모드 연습 이름 (ex. 1 + "회차"). coachingReportsEntity.coachingPracticeCnt + "회차"
        val coachingReportsEntity = coachingReportsRepository.findReportByPractice(practiceId)
            ?: throw PracticeException(PracticeError.REPORT_NOT_FOUND)

        // 3. 연습일시 (createdAt) 가져오기
        val practicesEntity = practicesRepository.findById(
            coachingReportsEntity.practicesEntity.practiceId
        ).orElseThrow { PracticeException(PracticeError.PRACTICE_NOT_FOUND) }

        // 4. minio의 record url
        // practiceId로 coaching report 조회 -> record_url을 /로 구분하기
//        val fileName = coachingReportsEntity.recordUrl
//        val parts = fileName.split("/")
//        val recordUrl = minioService.generatePresignedGetUrl(
//            parts[0],
//            parts[1]
//        )

        // 5. 성량 정보, 속도 정보, 휴지 횟수, 발음 점수
        val volumeStatus = coachingReportsEntity.speechVolume
        val speedStatus = coachingReportsEntity.speechSpeed
        val pauseCount = coachingReportsEntity.pauseCount
        val pronunciationScore = coachingReportsEntity.pronunciationScore

        return CoachingPracticeReport(
            projectName = projectEntity.title,
            practiceName = "${coachingReportsEntity.coachingPracticeCnt}회차",
            date = practicesEntity.createdAt,
            volumeStatus = volumeStatus,
            speedStatus = speedStatus,
            pauseCount = pauseCount,
            pronunciationScore = pronunciationScore
        ).toResponse()
    }

}