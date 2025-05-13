package com.ssafy.spico.domain.practice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_feedback_points")
public class VideoFeedbackPointsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer videoFeedbackPointId;

    // 연관 엔티티 참조 (외래키 제약 없이)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_report_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FinalReportsEntity finalReportsEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FeedbackType type; // 지적 사항 종류(VOLUME, SPEED, PAUSE)

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 지적 사항 발생 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 지적 사항 종료 시간

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_detail")
    private FeedbackDetail feedbackDetail; // 지적 세부 사항(SLOW, FAST, QUIET, LOUD, MIDDLE)

    public VideoFeedbackPointsEntity() {

    }

    public VideoFeedbackPointsEntity(FinalReportsEntity finalReportsEntity,
                                     FeedbackType type,
                                     LocalDateTime startTime,
                                     LocalDateTime endTime,
                                     FeedbackDetail feedbackDetail) {
        this.finalReportsEntity = finalReportsEntity;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.feedbackDetail = feedbackDetail;
    }

    public Integer getVideoFeedbackPointId() {
        return videoFeedbackPointId;
    }

    public FinalReportsEntity getFinalReportsEntity() {
        return finalReportsEntity;
    }

    public FeedbackType getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public FeedbackDetail getFeedbackDetail() {
        return feedbackDetail;
    }


}
