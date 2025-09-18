package com.example.task.dto;

import com.example.task.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;

/**
 * 클라이언트(예: 웹 브라우저, 모바일 앱)로부터 Task 생성 및 수정 요청을 받을 때
 * 사용되는 DTO(Data Transfer Object) 클래스입니다.
 * <p>
 * DTO를 사용함으로써 API의 요청 스펙을 명확히 하고,
 * 서비스 로직과 엔티티를 외부 노출로부터 보호할 수 있습니다.
 * </p>
 */
public class TaskRequestDto {

    /**
     * Task의 제목.
     * @NotBlank: null, "", " " (공백만 있는 문자열)을 허용하지 않습니다.
     * 만약 유효하지 않은 값이 들어오면 400 Bad Request 오류를 자동으로 발생시킵니다.
     */
    @NotBlank(message = "Title cannot be blank")
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

