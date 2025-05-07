package com.ssafy.spico.domain.project.entity;

import com.ssafy.spico.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

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

    public ProjectEntity(User user, String title, LocalDate date, LocalTime limitTime, String script, LocalDateTime createdAt) {
        this.user = user;
        this.title = title;
        this.date = date;
        this.limitTime = limitTime;
        this.script = script;
        this.createdAt = createdAt;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public User getUser() {
        return user;
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