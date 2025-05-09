package com.ssafy.spico.domain.project.entity;

import com.ssafy.spico.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity userEntity;

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "limit_time", nullable = false)
    private LocalTime limitTime;

    @Column(name = "script", columnDefinition = "TEXT")
    private String script;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    public ProjectEntity() {
    }

    public ProjectEntity(UserEntity userEntity, String title, LocalDate date, LocalTime limitTime, String script, LocalDateTime createdAt) {
        this.userEntity = userEntity;
        this.title = title;
        this.date = date;
        this.limitTime = limitTime;
        this.script = script;
        this.createdAt = createdAt;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getLimitTime() {
        return limitTime;
    }

    public String getScript() {
        return script;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}