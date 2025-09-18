package com.example.task.dto;

import com.example.task.entity.Task;
import com.example.task.entity.TaskStatus;
import java.time.LocalDateTime;

/**
 * 서버가 클라이언트에게 Task 정보를 응답할 때 사용하는 DTO 클래스입니다.
 * <p>
 * 엔티티 객체를 직접 반환하는 대신 이 DTO를 사용하면,
 * API 응답에 포함될 정보만을 선택적으로 가공하여 전달할 수 있습니다.
 * (예: 민감한 정보 제외, 필요한 정보 추가)
 * </p>
 */
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Integer priority;
    private String assignee;
    private Long viewCount; // Redis에서 가져온 조회수 정보
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Task 엔티티와 조회수 정보를 받아 DTO 객체를 생성하는 생성자입니다.
     * 서비스 계층에서 DB 정보와 Redis 정보를 조합하여 이 객체를 만듭니다.
     *
     * @param task      데이터베이스에서 조회한 Task 엔티티 객체
     * @param viewCount Redis에서 조회한 해당 Task의 조회수
     */
    public TaskResponseDto(Task task, Long viewCount) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.assignee = task.getAssignee();
        this.viewCount = viewCount;
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
    }
    
    // --- Getters and Setters ---
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
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

