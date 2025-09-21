# Task Management API - Kubernetes Project

## 프로젝트 개요
**개발 기간**: 4일 (32시간)  
**목표**: 컨테이너 오케스트레이션 실무 경험 습득

### 개발 목적
- 5-10명 규모 개발팀의 Task 관리
- Kubernetes 기반 자동 스케일링
- CI/CD 파이프라인 구축

## 기술 스택
- **Backend**: Spring Boot 3.x, Java 17
- **Database**: PostgreSQL 15, Redis 7
- **Container**: Docker, Kubernetes
- **CI/CD**: GitLab CI, Jenkins
- **Monitoring**: Metrics Server, K6




## Quick Start
# 1. 코드 클론
git clone https://gitlab.com/xogus9107/task-management-k8s.git

# 2. Docker 이미지 빌드
docker build -t task-api:v1 .

# 3. Kubernetes 배포
kubectl apply -f k8s/base/ -n task-app

# 4. 서비스 접근
kubectl port-forward service/task-api-service 8080:80 -n task-app

##API 엔드포인트
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET    | /api/tasks | 전체 조회 |
| POST   | /api/tasks | 생성 |
| PUT    | /api/tasks/{id} | 수정 |
| DELETE | /api/tasks/{id} | 삭제 |



## CI/CD 파이프라인
- GitLab CI: 자동 빌드/테스트
- Jenkins: 프로덕션 배포
- Docker Hub: 이미지 저장소

## 성능
- 처리량: 1,350 req/s
- P95 응답시간: 487ms
- 자동 스케일링: 2-10 pods
