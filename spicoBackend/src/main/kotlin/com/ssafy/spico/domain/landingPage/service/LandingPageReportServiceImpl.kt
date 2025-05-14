package com.ssafy.spico.domain.landingPage.service

import com.ssafy.spico.domain.landingPage.dto.LandingPageReportsResponseDto
import com.ssafy.spico.domain.landingPage.dto.toResponse
import com.ssafy.spico.domain.landingPage.model.LandingPageReports
import com.ssafy.spico.domain.landingPage.model.Reports
import com.ssafy.spico.domain.landingPage.repository.LandingPageReportRepository
import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.repository.CoachingReportsRepository
import com.ssafy.spico.domain.practice.repository.FinalReportsRepository
import org.springframework.stereotype.Service

@Service
class LandingPageReportServiceImpl(
    private val landingPageReportRepository: LandingPageReportRepository,
    private val finalReportsRepository: FinalReportsRepository,
    private val coachingReportsRepository: CoachingReportsRepository
) : LandingPageReportService {

    override fun landingPageReports(userId: Int) : LandingPageReportsResponseDto {

        val practicesEntity = landingPageReportRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId)

        val reports = practicesEntity.mapNotNull { practice ->
            val project = practice.projectEntity
            val type = practice.type

            when (type) {
                PracticeType.FINAL -> {
                    val final = finalReportsRepository.findReportByPractice(practice.practiceId)
                    final?.let {
                        Reports(
                            type = PracticeType.FINAL,
                            practiceId = practice.practiceId,
                            projectName = project.title,
                            practiceName = "${it.finalPracticeCnt}회차",
                            reportsId = it.finalReportId
                        )
                    }
                }

                PracticeType.COACHING -> {
                    val coaching = coachingReportsRepository.findReportByPractice(practice.practiceId)
                    coaching?.let {
                        Reports(
                            type = PracticeType.COACHING,
                            practiceId = practice.practiceId,
                            projectName = project.title,
                            practiceName = "${it.coachingPracticeCnt}회차",
                            reportsId = it.coachingReportId
                        )
                    }
                }

                else -> null
            }
        }


        return LandingPageReports(reports).toResponse()
    }

}