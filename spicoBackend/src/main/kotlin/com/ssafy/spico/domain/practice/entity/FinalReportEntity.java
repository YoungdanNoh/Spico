package com.ssafy.spico.domain.practice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "final_reports")
public class FinalReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer finalReportId;

    // 연관 엔티티 참조 (외래키 제약 없이)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practice_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PracticeEntity practiceEntity;

    @Column(name = "pause_score", nullable = false)
    private Integer pauseScore; // 휴지 기간 점수

    @Column(name = "pronunciation_score", nullable = false)
    private Integer pronunciationScore; // 발음 점수

    @Column(name = "speed_score", nullable = false)
    private Integer speedScore; // 발표 속도 점수

    @Column(name = "speech_speed", nullable = false)
    private String speechSpeed; // 발표 속도 피드백(적당해요, 너무 느려요 등)

    @Column(name = "volume_score", nullable = false)
    private Integer volumeScore; // 성량 점수

    @Column(name = "speech_volume", nullable = false)
    private String speechVolume; // 성량 피드백(적당해요, 너무 커요 등)

    @Column(name = "script_match_rate", nullable = false)
    private Integer scriptMatchRate; // 대본 일치도

    @Column(name = "pause_count", nullable = false)
    private Integer pauseCount; // 휴지 구간 횟수

    @Column(name = "total_score", nullable = false)
    private Integer totalScore; // 종합 점수

    @Column(name = "video_url", nullable = false)
    private String videoUrl; // 발표 영상 url

    @Column(name = "speech_time", nullable = false)
    private Time speechTime; // 발표 시간

    @Column(name = "script", columnDefinition = "TEXT")
    private String script; // STT된 발표 내용

    @Column(name = "final_practice_cnt", nullable = false)
    private Integer finalPracticeCnt; // 해당 프로젝트의 파이널 모드 연습 횟수
}
