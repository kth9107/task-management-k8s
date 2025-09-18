package com.example.task.entity;

/**
 * Task의 상태를 나타내는 Enum (열거형) 입니다.
 * <p>
 * Enum을 사용하면 상태 값을 문자열이나 숫자로 직접 다루는 것보다
 * 코드의 가독성과 타입 안정성을 높일 수 있습니다.
 * </p>
 */
public enum TaskStatus {
    /** 할 일 (기본 상태) */
    TODO,

    /** 진행 중 */
    IN_PROGRESS,

    /** 완료됨 */
    DONE
}

