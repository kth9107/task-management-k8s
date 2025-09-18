package com.example.task.repository;

import com.example.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Task 엔티티에 대한 데이터베이스 접근을 처리하는 Spring Data JPA 리포지토리입니다.
 * JpaRepository<Task, Long>를 상속받음으로써, Task 엔티티(관리 대상)의
 * 기본 키(Primary Key) 타입이 Long임을 명시합니다.
 * <p>
 * 이 인터페이스를 정의하는 것만으로도 기본적인 CRUD(Create, Read, Update, Delete) 메서드
 * (예: save(), findById(), findAll(), deleteById())가 자동으로 생성되어 주입됩니다.
 * </p>
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}

