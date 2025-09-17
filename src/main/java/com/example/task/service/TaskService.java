package com.example.task.service;

import com.example.task.dto.TaskRequestDto;
import com.example.task.dto.TaskResponseDto;
import com.example.task.entity.Task;
import com.example.task.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final String VIEW_COUNT_KEY_PREFIX = "task:view:count:";

    public TaskService(TaskRepository taskRepository, RedisTemplate<String, String> redisTemplate) {
        this.taskRepository = taskRepository;
        this.redisTemplate = redisTemplate;
    }

    // 모든 Task 조회
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskResponseDto(task, getViewCount(task.getId())))
                .collect(Collectors.toList());
    }

    // 단일 Task 조회 (조회수 증가)
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        
        // Redis에서 조회수 1 증가
        incrementViewCount(id);
        
        return new TaskResponseDto(task, getViewCount(id));
    }

    // Task 생성
    @Transactional
    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        Task task = new Task();
        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        if (requestDto.getStatus() != null) {
            task.setStatus(requestDto.getStatus());
        }
        task.setPriority(requestDto.getPriority());
        task.setAssignee(requestDto.getAssignee());

        Task savedTask = taskRepository.save(task);
        return new TaskResponseDto(savedTask, 0L); // 새로 생성된 Task의 조회수는 0
    }

    // Task 수정
    @Transactional
    public TaskResponseDto updateTask(Long id, TaskRequestDto requestDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        task.setStatus(requestDto.getStatus());
        task.setPriority(requestDto.getPriority());
        task.setAssignee(requestDto.getAssignee());

        Task updatedTask = taskRepository.save(task);
        return new TaskResponseDto(updatedTask, getViewCount(id));
    }

    // Task 삭제
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
        // Redis의 조회수 데이터도 함께 삭제
        redisTemplate.delete(VIEW_COUNT_KEY_PREFIX + id);
    }
    
    // --- Redis 조회수 관련 Helper 메서드 ---

    private void incrementViewCount(Long taskId) {
        String key = VIEW_COUNT_KEY_PREFIX + taskId;
        redisTemplate.opsForValue().increment(key);
    }

    private Long getViewCount(Long taskId) {
        String key = VIEW_COUNT_KEY_PREFIX + taskId;
        String count = redisTemplate.opsForValue().get(key);
        return count == null ? 0L : Long.parseLong(count);
    }
}
