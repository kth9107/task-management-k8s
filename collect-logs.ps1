$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$logDir = "logs-$timestamp"
New-Item -ItemType Directory -Path $logDir

# 애플리케이션 로그
kubectl logs deployment/task-api -n task-app --tail=1000 > "$logDir\app.log"
kubectl logs deployment/postgres -n task-app --tail=1000 > "$logDir\postgres.log"
kubectl logs deployment/redis -n task-app --tail=1000 > "$logDir\redis.log"

# 이벤트 로그
kubectl get events -n task-app --sort-by='.lastTimestamp' > "$logDir\events.log"

# 리소스 상태
kubectl describe all -n task-app > "$logDir\resources.log"

# Pod 상태
kubectl get pods -n task-app -o yaml > "$logDir\pods.yaml"

Write-Host "Logs collected in $logDir" -ForegroundColor Green
