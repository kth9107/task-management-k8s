package com.example.task.controller;

import com.example.task.dto.TaskRequestDto;
import com.example.task.dto.TaskResponseDto;
import com.example.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Task 관련 HTTP 요청을 처리하는 API 컨트롤러입니다.
 * @RestController: 이 클래스가 RESTful API의 엔드포인트임을 나타냅니다.
 * @RequestMapping("/api/tasks"): 이 컨트롤러의 모든 API는 공통적으로 /api/tasks 경로 하위에 위치하게 됩니다.
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
     * HTTP POST 요청을 /api/tasks 경로로 받습니다.
     *
     * @param requestDto HTTP 요청의 Body에 포함된 JSON 데이터를 TaskRequestDto 객체로 변환하여 받습니다.
     * @return 생성된 Task의 정보와 HTTP 상태 코드 201 (Created)를 포함하는 ResponseEntity를 반환합니다.
     */
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto createdTask = taskService.createTask(requestDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * 특정 ID를 가진 Task를 조회하는 API 엔드포인트입니다.
     * HTTP GET 요청을 /api/tasks/{id} 경로로 받습니다.
     *
     * @param id 경로 변수(Path Variable)로 전달된 조회할 Task의 ID.
     * @return 조회된 Task의 정보와 HTTP 상태 코드 200 (OK)를 반환합니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * 모든 Task 목록을 조회하는 API 엔드포인트입니다.
     * HTTP GET 요청을 /api/tasks 경로로 받습니다.
     *
     * @return 모든 Task 정보가 담긴 리스트와 HTTP 상태 코드 200 (OK)를 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * 특정 ID를 가진 Task를 수정하는 API 엔드포인트입니다.
     * HTTP PUT 요청을 /api/tasks/{id} 경로로 받습니다.
     *
     * @param id         수정할 Task의 ID.
     * @param requestDto 수정할 내용이 담긴 요청 Body.
     * @return 수정이 완료된 Task의 정보와 HTTP 상태 코드 200 (OK)를 반환합니다.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @RequestBody TaskRequestDto requestDto) {
        TaskResponseDto updatedTask = taskService.updateTask(id, requestDto);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * 특정 ID를 가진 Task를 삭제하는 API 엔드포인트입니다.
     * HTTP DELETE 요청을 /api/tasks/{id} 경로로 받습니다.
     *
     * @param id 삭제할 Task의 ID.
     * @return 내용 없이(no content) HTTP 상태 코드 204 (No Content)를 반환하여 성공을 알립니다.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

