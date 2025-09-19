package com.example.task.service;

import com.example.task.dto.TaskRequestDto;
import com.example.task.dto.TaskResponseDto;
import com.example.task.entity.Task;
import com.example.task.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Task 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 데이터베이스 트랜잭션 관리, 데이터 가공 등의 핵심 로직을 담당합니다.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final RedisTemplate<String, String> redisTemplate;
    // Redis 키의 충돌을 방지하고 명확하게 구분하기 위한 접두사입니다.
    private static final String VIEW_COUNT_KEY_PREFIX = "task:view:count:";

    /**
     * 생성자를 통한 의존성 주입(Dependency Injection)입니다.
     * Spring이 실행될 때 TaskRepository와 RedisTemplate의 구현체를 자동으로 주입해줍니다.
     *
     * @param taskRepository PostgreSQL DB 작업을 위한 리포지토리
     * @param redisTemplate  Redis 작업을 위한 템플릿
     */
    public TaskService(TaskRepository taskRepository, RedisTemplate<String, String> redisTemplate) {
        this.taskRepository = taskRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 등록된 모든 Task 목록을 조회합니다.
     * 각 Task의 조회수는 Redis에서 가져와 함께 반환합니다.
     *
     * @return TaskResponseDto 리스트
     */
    @Transactional(readOnly = true) // readOnly=true: 조회 성능을 최적화하는 트랜잭션 설정
    @Cacheable(value = "tasks", key = "'all'")  // key 추가
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskResponseDto(task, getViewCount(task.getId())))
                .collect(Collectors.toList());
    }

    /**
     * ID를 기준으로 특정 Task 한 개를 조회합니다.
     * 이 메서드가 호출될 때마다 해당 Task의 조회수가 1 증가합니다.
     *
     * @param id 조회할 Task의 ID
     * @return 조회된 Task의 정보 (조회수 포함)
     * @throws EntityNotFoundException 해당 ID의 Task가 없을 경우
     */
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long id) {
        Task task = findTaskOrThrow(id);
        incrementViewCount(id); // Redis의 조회수를 1 증가시킵니다.
        return new TaskResponseDto(task, getViewCount(id));
    }

    /**
     * 새로운 Task를 생성합니다.
     *
     * @param requestDto 생성할 Task의 정보가 담긴 DTO
     * @return 생성된 Task의 정보 (조회수는 0)
     */
    @Transactional // 데이터를 변경하므로 readOnly가 아닌 일반 트랜잭션 사용
    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        Task task = new Task();
        updateTaskFromDto(task, requestDto);
        if(task.getStatus() == null) {
            task.setStatus(com.example.task.entity.TaskStatus.TODO); // 생성 시 기본 상태를 TODO로 설정
        }

        Task savedTask = taskRepository.save(task);
        return new TaskResponseDto(savedTask, 0L); // 새로 생성된 Task의 조회수는 0입니다.
    }

    /**
     * 기존 Task의 정보를 수정합니다.
     *
     * @param id         수정할 Task의 ID
     * @param requestDto 수정할 내용이 담긴 DTO
     * @return 수정된 Task의 정보
     * @throws EntityNotFoundException 해당 ID의 Task가 없을 경우
     */
    @Transactional
    public TaskResponseDto updateTask(Long id, TaskRequestDto requestDto) {
        Task task = findTaskOrThrow(id);
        updateTaskFromDto(task, requestDto);
        Task updatedTask = taskRepository.save(task);
        return new TaskResponseDto(updatedTask, getViewCount(id));
    }

    /**
     * ID를 기준으로 특정 Task를 삭제합니다.
     * 데이터베이스의 Task 정보와 Redis의 조회수 정보를 모두 삭제합니다.
     *
     * @param id 삭제할 Task의 ID
     * @throws EntityNotFoundException 해당 ID의 Task가 없을 경우
     */
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("ID가 " + id + "인 Task를 찾을 수 없습니다.");
        }
        taskRepository.deleteById(id);
        redisTemplate.delete(VIEW_COUNT_KEY_PREFIX + id); // Redis에 저장된 조회수 데이터도 함께 삭제합니다.
    }
    
    // --- Private Helper Methods (내부 로직을 돕는 메서드) ---

    /**
     * ID로 Task를 찾고, 없으면 EntityNotFoundException을 발생시키는 Helper 메서드입니다.
     * @param id 찾을 Task의 ID
     * @return 찾아낸 Task 엔티티
     */
    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID가 " + id + "인 Task를 찾을 수 없습니다."));
    }

    /**
     * TaskRequestDto의 내용으로 Task 엔티티의 필드를 업데이트하는 Helper 메서드입니다.
     * @param task 업데이트할 Task 엔티티
     * @param dto  업데이트할 내용을 담은 DTO
     */
    private void updateTaskFromDto(Task task, TaskRequestDto dto) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setAssignee(dto.getAssignee());
    }

    /**
     * Redis에서 특정 Task의 조회수를 1 증가시킵니다.
     * Redis의 INCR 명령어를 사용하여 원자적(atomic)으로 연산을 처리합니다.
     * @param taskId 조회수를 증가시킬 Task의 ID
     */
    private void incrementViewCount(Long taskId) {
        String key = VIEW_COUNT_KEY_PREFIX + taskId;
        redisTemplate.opsForValue().increment(key);
    }

    /**
     * Redis에서 특정 Task의 현재 조회수를 가져옵니다.
     * 키가 존재하지 않으면(조회된 적이 없으면) 0을 반환합니다.
     * @param taskId 조회수를 가져올 Task의 ID
     * @return 조회수 (Long 타입)
     */
    private Long getViewCount(Long taskId) {
        String key = VIEW_COUNT_KEY_PREFIX + taskId;
        String countStr = redisTemplate.opsForValue().get(key);
        return countStr == null ? 0L : Long.parseLong(countStr);
    }
}

