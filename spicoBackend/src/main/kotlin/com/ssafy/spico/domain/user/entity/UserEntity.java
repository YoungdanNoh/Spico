package com.ssafy.spico.domain.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long kakaoId;

    private String nickname;

    private boolean hasAudience;

    private boolean hasQna;

    private Integer questionCount;

    private Integer answerTimeLimit;

    public UserEntity() {}

    public UserEntity(Long kakaoId, String nickname) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.hasAudience = true;
        this.hasQna = true;
        this.questionCount = 1;
        this.answerTimeLimit = 60;
    }

    public UserEntity(Integer id, Long kakaoId, String nickname, boolean hasAudience, boolean hasQna, int questionCount, int answerTimeLimit) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.hasAudience = hasAudience;
        this.hasQna = hasQna;
        this.questionCount = questionCount;
        this.answerTimeLimit = answerTimeLimit;
    }

    public Long getKakaoId() { return kakaoId; }

    public boolean isHasAudience() {
        return hasAudience;
    }

    public boolean isHasQna() {
        return hasQna;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public Integer getAnswerTimeLimit() {
        return answerTimeLimit;
    }

    public Integer getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void updateSetting(boolean hasAudience, boolean hasQna, int questionCount, int answerTimeLimit) {
        this.hasAudience = hasAudience;
        this.hasQna = hasQna;
        this.questionCount = questionCount;
        this.answerTimeLimit = answerTimeLimit;
    }
}