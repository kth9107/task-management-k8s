package com.example.task.repository;

import com.example.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // JpaRepository를 상속받는 것만으로 기본적인 CRUD 메서드(save, findById, findAll, deleteById 등)가 자동 생성됩니다.
}
