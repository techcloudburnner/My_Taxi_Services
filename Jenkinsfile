pipeline {
    agent any
    
    environment {
        IMAGE_NAME = "rohit261/rudrabannataxiservices"
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Build Jar') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh """
                docker build -t $IMAGE_NAME:$IMAGE_TAG .
                docker tag $IMAGE_NAME:$IMAGE_TAG $IMAGE_NAME:latest
                """
            }
        }
        
        stage('Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh """
                    echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                    docker push $IMAGE_NAME:$IMAGE_TAG
                    docker push $IMAGE_NAME:latest
                    """
                }
            }
            post {
                always {
                    sh 'docker logout || true'
                }
            }
        }
        
        stage('Deploy To Kubernetes') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig-file', variable: 'KUBECONFIG')]) {
                    sh """
                    kubectl --kubeconfig=$KUBECONFIG apply -f k8s/
                    
                    kubectl --kubeconfig=$KUBECONFIG \
                    set image deployment/taxi-backend \
                    taxi-backend=$IMAGE_NAME:$IMAGE_TAG
                    
                    if ! kubectl --kubeconfig=$KUBECONFIG \
                    rollout status deployment/taxi-backend --timeout=300s; then
                        echo "Deployment failed, rolling back..."
                        kubectl --kubeconfig=$KUBECONFIG rollout undo deployment/taxi-backend
                        exit 1
                    fi
                    """
                }
            }
        }
        
        stage('Verify Deployment') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig-file', variable: 'KUBECONFIG')]) {
                    sh """
                    kubectl --kubeconfig=$KUBECONFIG get pods
                    kubectl --kubeconfig=$KUBECONFIG get svc
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline Completed Successfully!'
        }
        failure {
            echo 'Pipeline Failed! Check logs for details.'
        }
        always {
            cleanWs()
        }
    }
}
