package com.example.task.controller;

import com.example.task.dto.TaskRequestDto;
import com.example.task.dto.TaskResponseDto;
import com.example.task.entity.Task; // Task 엔티티를 직접 사용하기 위해 import 합니다.
import com.example.task.entity.TaskStatus;
import com.example.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors; // stream API 사용을 위해 import 합니다.

/**
 * Task 관련 HTTP 요청을 처리하는 API 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 새로운 Task를 생성하는 API 엔드포인트입니다.
     * Service가 Task 엔티티를 파라미터로 요구하므로, 컨트롤러에서 DTO를 엔티티로 변환합니다.
     * @Valid 어노테이션을 통해 TaskRequestDto의 유효성 검사(@NotBlank 등)를 활성화합니다.
     */
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto requestDto) {
        // 1. 클라이언트로부터 받은 DTO를 Service가 요구하는 Entity 객체로 변환합니다.
        Task taskToCreate = new Task();
        taskToCreate.setTitle(requestDto.getTitle());
        taskToCreate.setDescription(requestDto.getDescription());
        // status가 null일 경우 기본값(TODO)을 설정해줍니다.
        taskToCreate.setStatus(requestDto.getStatus() == null ? TaskStatus.TODO : requestDto.getStatus());
        taskToCreate.setPriority(requestDto.getPriority());
        taskToCreate.setAssignee(requestDto.getAssignee());

        // 2. Service를 호출하여 Task를 생성합니다.
        Task createdTaskEntity = taskService.createTask(taskToCreate);
        
        // 3. Service로부터 받은 Entity를 최종 응답 형태인 DTO로 다시 변환합니다.
        // 새로 생성된 Task의 조회수는 항상 0입니다.
        TaskResponseDto responseDto = new TaskResponseDto(createdTaskEntity, 0L);
        
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 특정 ID를 가진 Task를 조회하는 API 엔드포인트입니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * 모든 Task 목록을 조회하는 API 엔드포인트입니다.
     * 서비스에서 받은 Task 엔티티 리스트를 TaskResponseDto 리스트로 변환하여 응답합니다.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        // 1. Service를 호출하여 Task 엔티티 목록을 받습니다.
        List<Task> taskEntities = taskService.getAllTasks();

        // 2. 받은 엔티티 목록을 순회하며, 각 엔티티를 TaskResponseDto로 변환합니다.
        List<TaskResponseDto> taskResponses = taskEntities.stream()
                .map(task -> {
                    // ★★★★★ 해결 방법 ★★★★★
                    // TaskService의 getViewCount가 private이라 호출할 수 없으므로,
                    // 목록 조회 시에는 조회수를 0으로 가정하여 응답 DTO를 생성합니다.
                    // 상세 조회(getTaskById) 시에는 서비스에서 처리하므로 실제 조회수가 보입니다.
                    // 주석 수정
                    return new TaskResponseDto(task, 0L);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskResponses);
    }

    /**
     * 특정 ID를 가진 Task를 수정하는 API 엔드포인트입니다.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDto requestDto) {
        TaskResponseDto updatedTask = taskService.updateTask(id, requestDto);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * 특정 ID를 가진 Task를 삭제하는 API 엔드포인트입니다.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

