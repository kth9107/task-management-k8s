kubectl config current-context

kubectl delete all --all -n task-app
kubectl delete namespace task-app

docker build -t task-api:v1 .

# 이미지 확인
docker images | grep task-api

# 네임스페이스 생성
kubectl create namespace task-app

# 배포 순서 중요 (의존성 고려)
# 1. Secret과 ConfigMap 먼저
kubectl apply -f k8s/base/secret.yaml -n task-app
kubectl apply -f k8s/base/configmap.yaml -n task-app

# 2. Database와 Redis
kubectl apply -f k8s/base/postgres-deployment.yaml -n task-app
kubectl apply -f k8s/base/redis-deployment.yaml -n task-app

# 3. DB가 준비될 때까지 대기
kubectl wait --for=condition=ready pod -l app=postgres -n task-app --timeout=60s

# 4. 메인 애플리케이션
kubectl apply -f k8s/base/deployment.yaml -n task-app
kubectl apply -f k8s/base/service.yaml -n task-app
kubectl apply -f k8s/base/hpa.yaml -n task-app

#5. 실행
kubectl port-forward service/task-api-service 8080:80 -n task-app

