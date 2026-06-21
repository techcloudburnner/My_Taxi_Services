pipeline {
    agent any
    
    environment {
        IMAGE_NAME = "rohit261/rudrabannataxiservices"
        IMAGE_TAG = "${BUILD_NUMBER}"
        KUBECONFIG = "/var/jenkins_home/.kube/config"
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
                echo "========================================="
                echo "BUILD VERSION: ${IMAGE_TAG}"
                echo "========================================="
            }
        }
        
        stage('Build Jar') {
            steps {
                sh '''
                    echo "========================================="
                    echo "BUILDING JAVA APPLICATION"
                    echo "========================================="
                    
                    mvn clean package -DskipTests -B
                    
                    echo ""
                    echo "Build Output:"
                    ls -la target/
                    
                    # Check if JAR exists
                    if ls target/*.jar 1> /dev/null 2>&1; then
                        echo ""
                        echo "✅ JAR file created successfully!"
                        # Show jar file name
                        echo "JAR File: $(ls target/*.jar | head -1)"
                    else
                        echo ""
                        echo "❌ No JAR file found!"
                        exit 1
                    fi
                '''
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh '''
                    echo "========================================="
                    echo "BUILDING DOCKER IMAGE"
                    echo "========================================="
                    
                    # Build Docker image
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                    
                    # Tag as latest
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                    
                    echo ""
                    echo "Docker Images:"
                    docker images | grep ${IMAGE_NAME}
                    
                    echo ""
                    echo "✅ Docker image built successfully!"
                '''
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
                    sh '''
                        echo "========================================="
                        echo "PUSHING TO DOCKER HUB"
                        echo "========================================="
                        
                        echo "Logging into Docker Hub..."
                        echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
                        
                        echo "Pushing ${IMAGE_NAME}:${IMAGE_TAG}..."
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        
                        echo "Pushing ${IMAGE_NAME}:latest..."
                        docker push ${IMAGE_NAME}:latest
                        
                        echo ""
                        echo "✅ Images pushed successfully!"
                    '''
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
                sh '''
                    echo "========================================="
                    echo "DEPLOYING TO KUBERNETES"
                    echo "========================================="
                    
                    # Check if kubeconfig exists
                    if [ ! -f "${KUBECONFIG}" ]; then
                        echo "❌ Kubeconfig not found at: ${KUBECONFIG}"
                        echo "Checking for kubeconfig in home directory..."
                        if [ -f "$HOME/.kube/config" ]; then
                            echo "Found kubeconfig at $HOME/.kube/config"
                            export KUBECONFIG="$HOME/.kube/config"
                        else
                            echo "No kubeconfig found!"
                            exit 1
                        fi
                    fi
                    
                    # Apply MySQL first
                    echo "[1/3] Deploying MySQL..."
                    kubectl --kubeconfig=${KUBECONFIG} apply -f k8s/mysql-deployment.yaml
                    kubectl --kubeconfig=${KUBECONFIG} apply -f k8s/mysql-service.yaml
                    
                    # Wait for MySQL
                    echo "[2/3] Waiting for MySQL to be ready..."
                    kubectl --kubeconfig=${KUBECONFIG} wait --for=condition=ready pod -l app=mysql --timeout=180s || {
                        echo "⚠️  MySQL might not be ready yet, continuing..."
                    }
                    
                    # Deploy application
                    echo "[3/3] Deploying Application..."
                    kubectl --kubeconfig=${KUBECONFIG} apply -f k8s/service.yaml
                    kubectl --kubeconfig=${KUBECONFIG} apply -f k8s/deployment.yaml
                    
                    # Update image
                    echo "Updating deployment image..."
                    kubectl --kubeconfig=${KUBECONFIG} \
                        set image deployment/taxi-backend \
                        taxi-backend=${IMAGE_NAME}:${IMAGE_TAG}
                    
                    # Wait for rollout
                    echo ""
                    echo "Waiting for rollout to complete..."
                    if ! kubectl --kubeconfig=${KUBECONFIG} \
                        rollout status deployment/taxi-backend --timeout=300s; then
                        echo ""
                        echo "========================================="
                        echo "❌ DEPLOYMENT FAILED!"
                        echo "========================================="
                        echo "Rolling back to previous version..."
                        kubectl --kubeconfig=${KUBECONFIG} rollout undo deployment/taxi-backend
                        
                        echo ""
                        echo "Failed pod information:"
                        kubectl --kubeconfig=${KUBECONFIG} get pods -l app=taxi-backend
                        
                        echo ""
                        echo "Pod details:"
                        kubectl --kubeconfig=${KUBECONFIG} describe pod -l app=taxi-backend
                        
                        echo ""
                        echo "Application logs (last 50 lines):"
                        kubectl --kubeconfig=${KUBECONFIG} logs -l app=taxi-backend --tail=50
                        
                        exit 1
                    fi
                    
                    echo ""
                    echo "✅ Deployment successful!"
                '''
            }
        }
        
        stage('Verify Deployment') {
            steps {
                sh '''
                    echo "========================================="
                    echo "VERIFYING DEPLOYMENT"
                    echo "========================================="
                    
                    echo ""
                    echo "📦 PODS STATUS:"
                    kubectl --kubeconfig=${KUBECONFIG} get pods -o wide
                    
                    echo ""
                    echo "🌐 SERVICES:"
                    kubectl --kubeconfig=${KUBECONFIG} get svc
                    
                    echo ""
                    echo "📊 DEPLOYMENTS:"
                    kubectl --kubeconfig=${KUBECONFIG} get deployments
                    
                    echo ""
                    echo "📋 APPLICATION LOGS (last 20 lines):"
                    kubectl --kubeconfig=${KUBECONFIG} logs -l app=taxi-backend --tail=20 --prefix=true || echo "No logs available yet"
                    
                    echo ""
                    echo "✅ Verification complete!"
                '''
            }
        }
    }
    
    post {
        success {
            echo """
                =========================================
                🎉 PIPELINE SUCCESSFUL!
                =========================================
                Application: Rudra Banna Taxi Services
                Image: ${IMAGE_NAME}:${IMAGE_TAG}
                Build Number: ${BUILD_NUMBER}
                Build Duration: ${currentBuild.durationString}
                Build URL: ${env.BUILD_URL}
                =========================================
                
                ✅ Maven Build - Completed
                ✅ Docker Build - Completed
                ✅ Docker Push - Completed
                ✅ Kubernetes Deploy - Completed
                ✅ Verification - Completed
            """
        }
        
        failure {
            echo """
                =========================================
                ❌ PIPELINE FAILED!
                =========================================
                Application: Rudra Banna Taxi Services
                Failed Stage: ${env.STAGE_NAME}
                Build Number: ${BUILD_NUMBER}
                Build URL: ${env.BUILD_URL}
                =========================================
                
                Please check the console output above for error details.
            """
        }
        
        always {
            echo "========================================="
            echo "CLEANING UP WORKSPACE"
            echo "========================================="
            cleanWs(
                cleanWhenNotBuilt: false,
                deleteDirs: true,
                notFailBuild: true
            )
            echo "Cleanup complete!"
        }
    }
}
