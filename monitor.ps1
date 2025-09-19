#실시간 모니터링 스크립트

while($true) {
    Clear-Host
    Write-Host "=== Task App Monitoring ===" -ForegroundColor Green
    Write-Host "Time: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Yellow
    Write-Host ""
    
    Write-Host "PODS:" -ForegroundColor Cyan
    kubectl get pods -n task-app
    
    Write-Host "`nHPA:" -ForegroundColor Cyan
    kubectl get hpa -n task-app
    
    Write-Host "`nSERVICES:" -ForegroundColor Cyan
    kubectl get svc -n task-app
    
    Start-Sleep -Seconds 3
}