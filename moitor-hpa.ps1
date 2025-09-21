Write-Host "Starting HPA monitoring..." -ForegroundColor Green
Write-Host "Run load test in another terminal" -ForegroundColor Yellow

while($true) {
    Clear-Host
    Write-Host "=== HPA Monitoring ===" -ForegroundColor Cyan
    Write-Host "Time: $(Get-Date -Format 'HH:mm:ss')" -ForegroundColor Yellow
    
    kubectl get hpa -n task-app
    Write-Host ""
    kubectl get pods -n task-app | Select-String "task-api"
    Write-Host ""
    kubectl top pods -n task-app | Select-String "task-api"
    
    Start-Sleep -Seconds 2
}
