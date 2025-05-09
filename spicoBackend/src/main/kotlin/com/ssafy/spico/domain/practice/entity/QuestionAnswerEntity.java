package com.ssafy.spico.domain.practice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question_answer")
public class QuestionAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_answer_id")
    private Integer questionAnswerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_report_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FinalReportEntity finalReportEntity;

    @Column(name = "question", columnDefinition = "TEXT")
    private String question;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    public QuestionAnswerEntity(FinalReportEntity finalReportEntity, String question) {
        this.finalReportEntity = finalReportEntity;
        this.question = question;
    }
}
