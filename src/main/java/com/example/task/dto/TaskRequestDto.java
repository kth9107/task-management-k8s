package com.example.task.dto;

import com.example.task.entity.TaskStatus;

// 클라이언트로부터 Task 생성 및 수정 요청을 받을 때 사용하는 DTO
public class TaskRequestDto {
    private String title;
    private String description;
    private TaskStatus status;
    private Integer priority;
    private String assignee;

    // --- Getters and Setters ---
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
}
