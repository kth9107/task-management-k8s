package com.example.task.entity;
import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 'tasks' 데이터베이스 테이블과 매핑되는 JPA 엔티티 클래스입니다.
 * Task 한 개의 정보를 나타냅니다.
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Task의 고유 식별자 (Primary Key) 입니다.
     * IDENTITY 전략은 데이터베이스가 ID 생성을 책임지도록 합니다. (예: PostgreSQL의 SERIAL)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Task의 제목. null 값을 허용하지 않습니다. */
    @Column(nullable = false, length = 255)
    private String title;

    /** Task의 상세 설명. */
    @Column(length = 2000)
    private String description;

    /**
     * Task의 현재 상태. Enum 타입이며, 데이터베이스에는 문자열(STRING)로 저장됩니다.
     * 기본값으로 TaskStatus.TODO가 설정됩니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    /** Task의 우선순위. 숫자가 낮을수록 우선순위가 높음을 의미할 수 있습니다. */
    private Integer priority;

    /** Task를 담당하는 사용자의 이름. */
    @Column(length = 100)
    private String assignee;

    /**
     * Task가 생성된 시각.
     * @CreationTimestamp 어노테이션에 의해 엔티티가 처음 저장될 때 자동으로 현재 시각이 기록됩니다.
     * updatable = false 설정은 이 필드가 수정되지 않도록 합니다.
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * Task 정보가 마지막으로 수정된 시각.
     * @UpdateTimestamp 어노테이션에 의해 엔티티가 수정될 때마다 자동으로 현재 시각이 기록됩니다.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // --- Getters and Setters ---
    // 실제 프로젝트에서는 코드를 간결하게 하기 위해 Lombok 라이브러리 (@Getter, @Setter 등) 사용을 적극 권장합니다.
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

