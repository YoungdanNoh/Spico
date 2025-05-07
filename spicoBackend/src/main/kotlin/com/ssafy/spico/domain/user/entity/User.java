package com.ssafy.spico.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private boolean hasAudience;

    private boolean hasQna;

    private Integer questionCount;

    private Integer answerTimeLimit;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.hasAudience = true;
        this.hasQna = true;
        this.questionCount = 1;
        this.answerTimeLimit = 1;
    }

    public void updateHasAudience(boolean hasAudience) {
        this.hasAudience = hasAudience;
    }

    public void updateHasQna(boolean hasQna) {
        this.hasQna = hasQna;
        if(this.hasQna){
            this.questionCount = 1;
            this.answerTimeLimit = 1;
        }
    }

    public void updateQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public void updateAnswerTimeLimit(int answerTimeLimit) {
        this.answerTimeLimit = answerTimeLimit;
    }
}