package com.ssafy.spico.domain.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Time;

@Entity
@Table(name = "final_reports")
public class FinalReportsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer finalReportId;

    // 연관 엔티티 참조 (외래키 제약 없이)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practice_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PracticesEntity practicesEntity;

    @Column(name = "pause_score")
    private Integer pauseScore; // 휴지 기간 점수

    @Column(name = "pronunciation_score")
    private Integer pronunciationScore; // 발음 점수

    @Column(name = "speed_score")
    private Integer speedScore; // 발표 속도 점수

    @Enumerated(EnumType.STRING)
    @Column(name = "speech_speed")
    private SpeedType speechSpeed; // 발표 속도 피드백(SLOW, FAST)

    @Column(name = "volume_score")
    private Integer volumeScore; // 성량 점수

    @Enumerated(EnumType.STRING)
    @Column(name = "speech_volume")
    private VolumeType speechVolume; // 성량 피드백(LOUD, QUIET)

    @Column(name = "script_match_rate")
    private Integer scriptMatchRate; // 대본 일치도

    @Column(name = "pause_count")
    private Integer pauseCount; // 휴지 구간 횟수

    @Column(name = "total_score")
    private Integer totalScore; // 종합 점수

    @Column(name = "video_url")
    private String videoUrl; // 발표 영상 url

    @Column(name = "script", columnDefinition = "TEXT")
    private String script; // STT된 발표 내용

    @Column(name = "final_practice_cnt", nullable = false)
    private Integer finalPracticeCnt; // 해당 프로젝트의 파이널 모드 연습 횟수

    public FinalReportsEntity() {
    }

    public FinalReportsEntity(PracticesEntity practicesEntity, Integer finalPracticeCnt, String script) {
        this.practicesEntity = practicesEntity;
        this.finalPracticeCnt = finalPracticeCnt;
        this.script = script;
    }

    public Integer getFinalReportId() {
        return finalReportId;
    }

    public PracticesEntity getPracticesEntity() {
        return practicesEntity;
    }

    public Integer getPauseScore() {
        return pauseScore;
    }

    public Integer getPronunciationScore() {
        return pronunciationScore;
    }

    public Integer getSpeedScore() {
        return speedScore;
    }

    public SpeedType getSpeechSpeed() {
        return speechSpeed;
    }

    public Integer getVolumeScore() {
        return volumeScore;
    }

    public VolumeType getSpeechVolume() {
        return speechVolume;
    }

    public Integer getScriptMatchRate() {
        return scriptMatchRate;
    }

    public Integer getPauseCount() {
        return pauseCount;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getScript() {
        return script;
    }

    public Integer getFinalPracticeCnt() {
        return finalPracticeCnt;
    }

    public void setPauseScore(Integer pauseScore) {
        this.pauseScore = pauseScore;
    }

    public void setPronunciationScore(Integer pronunciationScore) {
        this.pronunciationScore = pronunciationScore;
    }

    public void setSpeedScore(Integer speedScore) {
        this.speedScore = speedScore;
    }

    public void setSpeechSpeed(SpeedType speechSpeed) {
        this.speechSpeed = speechSpeed;
    }

    public void setVolumeScore(Integer volumeScore) {
        this.volumeScore = volumeScore;
    }

    public void setSpeechVolume(VolumeType speechVolume) {
        this.speechVolume = speechVolume;
    }

    public void setScriptMatchRate(Integer scriptMatchRate) {
        this.scriptMatchRate = scriptMatchRate;
    }

    public void setPauseCount(Integer pauseCount) {
        this.pauseCount = pauseCount;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setScript(String script) {
        this.script = script;
    }

}
