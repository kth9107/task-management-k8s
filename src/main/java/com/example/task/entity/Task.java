package com.example.task.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tasks") // 데이터베이스 테이블 이름을 'tasks'로 지정
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @Column(nullable = false)
    private String title; // 제목

    @Column(length = 1000)
    private String description; // 설명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO; // 상태 (기본값: TODO)

    private Integer priority; // 우선순위

    private String assignee; // 담당자

    @CreationTimestamp // 엔티티 생성 시각을 자동 기록
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // 엔티티 수정 시각을 자동 기록
    private LocalDateTime updatedAt;

    // --- Getters and Setters ---
    // Lombok 라이브러리를 사용하면 생략 가능합니다.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
