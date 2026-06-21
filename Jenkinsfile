pipeline {
    agent any
    
    environment {
        IMAGE_NAME = "rohit261/rudrabannataxiservices"
        IMAGE_TAG = "${BUILD_NUMBER}"
        KUBECONFIG = "/var/jenkins_home/.kube/config"
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
                sh """
                kubectl --kubeconfig=$KUBECONFIG delete deployment mysql --ignore-not-found=true
                kubectl --kubeconfig=$KUBECONFIG apply -f k8s/mysql-deployment.yaml
                kubectl --kubeconfig=$KUBECONFIG apply -f k8s/mysql-service.yaml
                
                kubectl --kubeconfig=$KUBECONFIG wait --for=condition=ready pod -l app=mysql --timeout=120s
                
                kubectl --kubeconfig=$KUBECONFIG apply -f k8s/service.yaml
                kubectl --kubeconfig=$KUBECONFIG apply -f k8s/deployment.yaml
                
                kubectl --kubeconfig=$KUBECONFIG set image deployment/taxi-backend taxi-backend=$IMAGE_NAME:$IMAGE_TAG
                
                kubectl --kubeconfig=$KUBECONFIG rollout status deployment/taxi-backend --timeout=300s
                """
            }
        }
        
        stage('Verify Deployment') {
            steps {
                sh """
                kubectl --kubeconfig=$KUBECONFIG get pods
                kubectl --kubeconfig=$KUBECONFIG get svc
                """
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline Completed Successfully'
        }
        failure {
            echo 'Pipeline Failed'
        }
        always {
            cleanWs()
        }
    }
}
