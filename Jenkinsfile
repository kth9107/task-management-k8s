pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'xogus9107/task-api'
        DOCKER_TAG = "${BUILD_NUMBER}"
        K8S_NAMESPACE = 'task-app'
        GITLAB_REPO = 'https://gitlab.com/xogus9107/task-management-k8s.git'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: "${GITLAB_REPO}",
                        credentialsId: 'gitlab-token'
                    ]]
                ])
            }
        }
        
        stage('Build') {
            steps {
                script {
                    sh './gradlew clean build -x test'
                }
            }
        }
        
        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit 'build/test-results/test/**/*.xml'
                }
            }
        }
        
        stage('Docker Build & Push') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                        def customImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                        customImage.push()
                        customImage.push('latest')
                    }
                }
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                    sh """
                        kubectl --kubeconfig=$KUBECONFIG set image deployment/task-api \
                        task-api=${DOCKER_IMAGE}:${DOCKER_TAG} -n ${K8S_NAMESPACE}
                        
                        kubectl --kubeconfig=$KUBECONFIG rollout status \
                        deployment/task-api -n ${K8S_NAMESPACE}
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            cleanWs()
        }
    }
}
