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
                sh """
                if [ -f /var/jenkins_home/.kube/config ]; then
                    KUBECONFIG=/var/jenkins_home/.kube/config
                elif [ -f /root/.kube/config ]; then
                    KUBECONFIG=/root/.kube/config
                else
                    echo "No kubeconfig found!"
                    exit 1
                fi
                
                export KUBECONFIG
                
                kubectl apply -f k8s/mysql-deployment.yaml
                kubectl apply -f k8s/mysql-service.yaml
                
                sleep 30
                
                kubectl apply -f k8s/service.yaml
                kubectl apply -f k8s/deployment.yaml
                
                kubectl set image deployment/taxi-backend taxi-backend=$IMAGE_NAME:$IMAGE_TAG
                kubectl rollout status deployment/taxi-backend --timeout=300s
                """
            }
        }
        
        stage('Verify Deployment') {
            steps {
                sh """
                if [ -f /var/jenkins_home/.kube/config ]; then
                    KUBECONFIG=/var/jenkins_home/.kube/config
                elif [ -f /root/.kube/config ]; then
                    KUBECONFIG=/root/.kube/config
                fi
                
                export KUBECONFIG
                
                kubectl get pods
                kubectl get svc
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
