package com.ssafy.spico.domain.practice.entity;

import com.ssafy.spico.domain.project.entity.ProjectEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "practices")
public class PracticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer practiceId;

    // 연관 엔티티 참조 (외래키 제약 없이)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProjectEntity projectEntity;


    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PracticeType type; // 종류 (예: 통합, 파이널)

    @Column(name = "status", nullable = false)
    private PracticeStatus status; // 상태 (진행중, 완료 등)

    public PracticeEntity(ProjectEntity projectEntity, LocalDateTime createdAt, PracticeType type, PracticeStatus status) {
        this.projectEntity = projectEntity;
        this.createdAt = createdAt;
        this.type = type;
        this.status = status;
    }

    public Integer getPracticeId() {
        return practiceId;
    }

    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PracticeType getType() {
        return type;
    }

    public PracticeStatus getStatus() {
        return status;
    }
}
