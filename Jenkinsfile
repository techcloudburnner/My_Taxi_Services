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
                    
                    echo "Build Output:"
                    ls -la target/
                    
                    # Check if JAR exists
                    if ls target/*.jar 1> /dev/null 2>&1; then
                        echo "✅ JAR file created successfully!"
                    else
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
                    
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                    
                    echo "Docker Images:"
                    docker images | grep ${IMAGE_NAME}
                    
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
                        exit 1
                    fi
                    
                    # Apply all Kubernetes manifests
                    echo "Applying Kubernetes configurations..."
                    kubectl --kubeconfig=${KUBECONFIG} apply -f k8s/
                    
                    # Update the deployment image
                    echo "Updating deployment image..."
                    kubectl --kubeconfig=${KUBECONFIG} \
                        set image deployment/taxi-backend \
                        taxi-backend=${IMAGE_NAME}:${IMAGE_TAG}
                    
                    # Wait for rollout to complete
                    echo "Waiting for rollout to complete..."
                    if ! kubectl --kubeconfig=${KUBECONFIG} \
                        rollout status deployment/taxi-backend --timeout=300s; then
                        echo ""
                        echo "❌ DEPLOYMENT FAILED!"
                        echo "Rolling back to previous version..."
                        kubectl --kubeconfig=${KUBECONFIG} rollout undo deployment/taxi-backend
                        
                        echo ""
                        echo "Failed pod information:"
                        kubectl --kubeconfig=${KUBECONFIG} get pods -l app=taxi-backend
                        
                        echo ""
                        echo "Pod logs:"
                        kubectl --kubeconfig=${KUBECONFIG} logs -l app=taxi-backend --tail=50
                        
                        exit 1
                    fi
                    
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
                
                ✅ All stages completed successfully
                ✅ Docker image pushed to registry
                ✅ Application deployed to Kubernetes
                ✅ Deployment verified
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
                
                Common fixes:
                1. Check Maven build errors
                2. Verify Docker daemon is running
                3. Check Docker Hub credentials
                4. Verify Kubernetes cluster connection
                5. Check if MySQL is running in cluster
            """
        }
        
        always {
            echo "========================================="
            echo "CLEANING UP WORKSPACE"
            echo "========================================="
            cleanWs(
                cleanWhenNotBuilt: false,
                deleteDirs: true,
                disableDeferredWipeout: false,
                notFailBuild: true
            )
            echo "Cleanup complete!"
        }
    }
}
