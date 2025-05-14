package com.ssafy.spico.domain.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "coaching_reports")
public class CoachingReportsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CoachingReportId;

    // 연관 엔티티 참조 (외래키 제약 없이)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practice_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PracticesEntity practicesEntity;

    @Column(name = "pronunciation_score")
    private Integer pronunciationScore; // 발음 점수

    @Column(name = "pause_count")
    private Integer pauseCount; // 휴지 구간 횟수

    @Enumerated(EnumType.STRING)
    @Column(name = "speech_speed")
    private SpeedType speechSpeed; // 발표 속도 피드백(SLOW, MIDDLE, FAST)

    @Enumerated(EnumType.STRING)
    @Column(name = "speech_volume")
    private VolumeType speechVolume; // 성량 피드백(QUIET, MIDDLE, LOUD)

    @Column(name = "record_url")
    private String recordUrl; // 발표 음성 url

    @Column(name = "coaching_practice_cnt", nullable = false)
    private Integer coachingPracticeCnt; // 해당 프로젝트의 코칭 모드 연습 횟수

    public CoachingReportsEntity() {

    }

    public CoachingReportsEntity(PracticesEntity practicesEntity,
                                 Integer pronunciationScore,
                                 Integer pauseCount,
                                 SpeedType speechSpeed,
                                 VolumeType speechVolume,
                                 String recordUrl,
                                 Integer coachingPracticeCnt) {
        this.practicesEntity = practicesEntity;
        this.pronunciationScore = pronunciationScore;
        this.pauseCount = pauseCount;
        this.speechSpeed = speechSpeed;
        this.speechVolume = speechVolume;
        this.recordUrl = recordUrl;
        this.coachingPracticeCnt = coachingPracticeCnt;
    }

    public Integer getCoachingReportId() {
        return CoachingReportId;
    }

    public PracticesEntity getPracticesEntity() {
        return practicesEntity;
    }

    public Integer getPronunciationScore() {
        return pronunciationScore;
    }

    public Integer getPauseCount() {
        return pauseCount;
    }

    public SpeedType getSpeechSpeed() {
        return speechSpeed;
    }

    public VolumeType getSpeechVolume() {
        return speechVolume;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public Integer getCoachingPracticeCnt() {
        return coachingPracticeCnt;
    }
}
