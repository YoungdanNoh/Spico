package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.entity.*
import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.model.*
import com.ssafy.spico.domain.practice.repository.*
import com.ssafy.spico.domain.project.repository.ProjectRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CoachingPracticeServiceImpl (
    private val projectRepository: ProjectRepository,
    private val practicesRepository: PracticesRepository,
    private val coachingReportsRepository: CoachingReportsRepository,
    private val minioService: MinioService,
) : CoachingPracticeService {

    @Transactional
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

    @Transactional
    override fun endCoachingPractice(
        userId: Int,
        projectId: Int,
        practiceId: Int,
        endCoachingPractice: EndCoachingPractice,
    ): EndCoachingPracticeResponseDto {

        // 1. projectEntity 의 last_final_cnt 값을 1 증가시키기
        val projectEntity = projectRepository.findById(projectId)
            .orElseThrow { PracticeException(PracticeError.PROJECT_NOT_FOUND) } //프로젝트 정보 가져오기
        projectEntity.lastCoachingCnt += 1

        // 2. practice_id로 practices 테이블 조회
        //    해당 practice의 상태를 COMPLETED로 변경
        val practiceEntity = practicesRepository.findById(practiceId)
            .orElseThrow { PracticeException(PracticeError.PRACTICE_NOT_FOUND) }
        practiceEntity.setStatus(PracticeStatus.COMPLETED)

        // 이미 완료된 상태라면 예외 발생
        if (practiceEntity.status == PracticeStatus.COMPLETED) {
            throw PracticeException(PracticeError.ALREADY_COMPLETED_PRACTICE)
        }

        // 3. minio에 영상 저장, url db에 넣기
        if(endCoachingPractice.fileName.isNullOrBlank()) {
            throw PracticeException(PracticeError.INVALID_FILENAME)
        }

        if (endCoachingPractice.pronunciationScore != null && endCoachingPractice.pronunciationScore !in 0..100) {
            throw PracticeException(PracticeError.INVALID_PRONUNCIATION_SCORE)
        }

        if (endCoachingPractice.pauseCount != null && endCoachingPractice.pauseCount < 0) {
            throw PracticeException(PracticeError.INVALID_PAUSE_COUNT)
        }

        if (endCoachingPractice.speedStatus != null && endCoachingPractice.speedStatus !in SpeedType.values()) {
            throw PracticeException(PracticeError.INVALID_SPEED_STATUS)
        }

        if (endCoachingPractice.volumeStatus != null && endCoachingPractice.volumeStatus !in VolumeType.values()) {
            throw PracticeException(PracticeError.INVALID_VOLUME_STATUS)
        }

        val presignedUrl = minioService.generatePresignedUrl("record", endCoachingPractice.fileName)
        val playbackUrl = "record/${endCoachingPractice.fileName}"

        // 4. coachingReportsEntity의 coaching_practice_cnt 값을 projectEntity.lastCoachingCnt로 넣기
        //    해당 practiceId로 coachingReports에 데이터 새로 추가
        //    발음 점수, 휴지 기간 횟수, 성량(String), 속도(String) db에 넣기
        val coachingReport = CoachingReportsEntity(
            practiceEntity,
            endCoachingPractice.pronunciationScore,
            endCoachingPractice.pauseCount,
            endCoachingPractice.speedStatus,
            endCoachingPractice.volumeStatus,
            playbackUrl,
            projectEntity.lastCoachingCnt
        )
        coachingReportsRepository.save(coachingReport)

        //println(minioService.generatePresignedGetUrl("record", endCoachingPractice.fileName))

        return PresignedUrl(
            presignedUrl = presignedUrl
        ).toEndCoachingResponse()
    }

}