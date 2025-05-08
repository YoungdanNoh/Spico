package com.ssafy.spico.domain.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String name;

    private boolean hasAudience;

    private boolean hasQna;

    private Integer questionCount;

    private Integer answerTimeLimit;

    public UserEntity() {}

    public UserEntity(String email, String name) {
        this.email = email;
        this.name = name;
        this.hasAudience = true;
        this.hasQna = true;
        this.questionCount = 1;
        this.answerTimeLimit = 1;
    }

    public UserEntity(String email, String name, boolean hasAudience, boolean hasQna, int questionCount, int answerTimeLimit) {
        this.email = email;
        this.name = name;
        this.hasAudience = hasAudience;
        this.hasQna = hasQna;
        this.questionCount = questionCount;
        this.answerTimeLimit = answerTimeLimit;
    }

    public String getEmail() {
        return email;
    }

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

    public String getName() {
        return name;
    }

    public void updateSetting(boolean hasAudience, boolean hasQna, int questionCount, int answerTimeLimit) {
        this.hasAudience = hasAudience;
        this.hasQna = hasQna;
        this.questionCount = questionCount;
        this.answerTimeLimit = answerTimeLimit;
    }
}