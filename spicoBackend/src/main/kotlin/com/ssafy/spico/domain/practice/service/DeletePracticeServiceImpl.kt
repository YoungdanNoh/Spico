package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.repository.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class DeletePracticeServiceImpl(
    private val practicesRepository: PracticesRepository,
    private val coachingReportsRepository: CoachingReportsRepository,
    private val finalReportsRepository: FinalReportsRepository,
    private val questionAnswerRepository: QuestionAnswerRepository,
    private val videoFeedbackPointsRepository: VideoFeedbackPointsRepository
) : DeletePracticeService {

    @Transactional
    override fun deletePractice(practiceId: Int) {

        val practiceEntity = practicesRepository.findById(practiceId)
            .orElseThrow{ PracticeException(PracticeError.PRACTICE_NOT_FOUND) }

        // 연관된 리포트 테이블, 피드백 테이블, 답변 테이블 삭제
        when (practiceEntity.type){
            PracticeType.COACHING -> {
                coachingReportsRepository.findReportByPractice(practiceId)
                    ?.let { coachingReportsRepository.delete(it) }
            }

            PracticeType.FINAL -> {
                finalReportsRepository.findReportByPractice(practiceId)
                    ?.let { finalReport ->
                        questionAnswerRepository.deleteAllByFinalReportsEntity(finalReport)
                        videoFeedbackPointsRepository.deleteAllByFinalReportsEntity(finalReport)
                        finalReportsRepository.delete(finalReport)
                    }
            }
        }

        // 연습 삭제
        practicesRepository.delete(practiceEntity)
    }
}