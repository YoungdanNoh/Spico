package com.ssafy.spico.domain.user.entity;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String name;

    private boolean hasAudience;

    private boolean hasQna;

    private Integer questionCount;

    private Integer answerTimeLimit;

    public User() {}

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.hasAudience = true;
        this.hasQna = true;
        this.questionCount = 1;
        this.answerTimeLimit = 1;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
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
}