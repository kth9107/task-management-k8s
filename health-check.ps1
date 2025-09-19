#헬스체크 스크립트

$namespace = "task-app"
$pods = kubectl get pods -n $namespace -o json | ConvertFrom-Json

foreach ($pod in $pods.items) {
    $podName = $pod.metadata.name
    $ready = $pod.status.containerStatuses[0].ready
    
    if ($ready) {
        Write-Host "$podName is healthy" -ForegroundColor Green
    } else {
        Write-Host "$podName is not healthy" -ForegroundColor Red
        kubectl describe pod $podName -n $namespace
    }
}