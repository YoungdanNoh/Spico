package com.ssafy.spico.domain.randomSpeech.entity;

import com.ssafy.spico.domain.randomSpeech.model.UpdateNewsCommand;
import com.ssafy.spico.domain.randomSpeech.model.UpdateQuestionCommand;
import com.ssafy.spico.domain.randomSpeech.model.UpdateResultCommand;
import com.ssafy.spico.domain.user.entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "random_speeches")
public class RandomSpeechEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer randomSpeechId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Topic topic;

    @Column(name = "speech_time", nullable = false)
    private Integer speechTime;

    @Column(name = "preparation_time", nullable = false)
    private Integer preparationTime;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "news_title", columnDefinition = "TEXT")
    private String newsTitle;

    @Column(name = "news_url", columnDefinition = "TEXT")
    private String newsUrl;

    @Column(name = "news_summary", columnDefinition = "TEXT")
    private String newsSummary;

    @Column(name = "question")
    private String question;

    @Column(name = "script", columnDefinition = "TEXT")
    private String script;

    @Column(name = "ai_feedback", columnDefinition = "TEXT")
    private String aiFeedback;

    @Column(name = "ai_title", length = 20)
    private String aiTitle;

    public RandomSpeechEntity() {
    }

    public RandomSpeechEntity(UserEntity userEntity, Topic topic, Integer speechTime, Integer preparationTime,
                              LocalDateTime createdAt, String newsTitle, String newsUrl, String newsSummary,
                              String question, String script, String aiFeedback, String aiTitle) {
        this.userEntity = userEntity;
        this.topic = topic;
        this.speechTime = speechTime;
        this.preparationTime = preparationTime;
        this.createdAt = createdAt;
        this.newsTitle = newsTitle;
        this.newsUrl = newsUrl;
        this.newsSummary = newsSummary;
        this.question = question;
        this.script = script;
        this.aiFeedback = aiFeedback;
        this.aiTitle = aiTitle;
    }

    public RandomSpeechEntity(UserEntity userEntity, Topic topic, Integer speechTime,
                              Integer preparationTime, LocalDateTime createdAt) {
        this.userEntity = userEntity;
        this.topic = topic;
        this.speechTime = speechTime;
        this.preparationTime = preparationTime;
        this.createdAt = createdAt;
    }

    public Integer getRandomSpeechId() { return randomSpeechId; }

    public UserEntity getUserEntity() { return userEntity; }

    public Topic getTopic() { return topic; }

    public Integer getSpeechTime() { return speechTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Integer getPreparationTime() { return preparationTime; }

    public String getNewsTitle() { return newsTitle; }

    public String getNewsUrl() { return newsUrl; }

    public String getNewsSummary() { return newsSummary; }

    public String getQuestion() { return question; }

    public String getScript() { return script; }

    public String getAiFeedback() { return aiFeedback; }

    public String getAiTitle() { return aiTitle; }

    public void updateNews(UpdateNewsCommand command) {
        this.newsTitle = command.getTitle();
        this.newsUrl = command.getUrl();
        this.newsSummary = command.getSummary();
    }

    public void updateQuestion(UpdateQuestionCommand command) {
        this.question = command.getQuestion();
    }

    public void updateResult(UpdateResultCommand command) {
        this.script = command.getScript();
        this.aiFeedback = command.getFeedback();
        this.aiTitle = command.getTitle();
    }
}