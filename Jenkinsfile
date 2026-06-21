pipeline {
    agent any
    
    environment {
        IMAGE_NAME = "rohit261/rudrabannataxiservices"
        IMAGE_TAG = "${BUILD_NUMBER}"
        DOCKER_BUILDKIT = '1'
        COMPOSE_DOCKER_CLI_BUILD = '1'
    }
    
    stages {
        stage('Checkout & Setup') {
            steps {
                checkout scm
                echo "Building version: ${IMAGE_TAG}"
                sh '''
                    echo "========================================="
                    echo "Environment Info"
                    echo "========================================="
                    java -version || echo "Java not found locally"
                    echo "Workspace contents:"
                    ls -la
                '''
            }
        }
        
        stage('Build with Maven Container') {
            steps {
                script {
                    docker.image('maven:3.8.6-openjdk-17-slim').inside('-v /var/run/docker.sock:/var/run/docker.sock --net=host') {
                        sh '''
                            echo "========================================="
                            echo "MAVEN BUILD STARTING"
                            echo "========================================="
                            
                            # Show Maven version
                            mvn --version
                            
                            # Clean and package
                            mvn clean package -DskipTests -B -e
                            
                            echo "========================================="
                            echo "BUILD OUTPUT"
                            echo "========================================="
                            ls -la target/
                            
                            # Verify jar exists
                            if [ ! -f target/*.jar ]; then
                                echo "❌ No JAR file found in target directory!"
                                exit 1
                            fi
                        '''
                    }
                }
            }
        }
        
        stage('Docker Build & Test') {
            steps {
                sh '''
                    echo "========================================="
                    echo "DOCKER BUILD STARTING"
                    echo "========================================="
                    
                    # Build Docker image
                    docker build \
                        -t ${IMAGE_NAME}:${IMAGE_TAG} \
                        -t ${IMAGE_NAME}:latest \
                        --build-arg BUILDKIT_INLINE_CACHE=1 \
                        .
                    
                    echo "Docker images:"
                    docker images | grep ${IMAGE_NAME}
                '''
            }
        }
        
        stage('Push to Docker Hub') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo "Logging into Docker Hub..."
                        echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
                        
                        echo "Pushing version: ${IMAGE_TAG}"
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        
                        echo "Pushing latest"
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
        
        stage('Deploy to Kubernetes') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([file(credentialsId: 'kubeconfig-file', variable: 'KUBECONFIG')]) {
                    sh '''
                        echo "========================================="
                        echo "DEPLOYING TO KUBERNETES"
                        echo "========================================="
                        
                        # Create namespace if not exists
                        kubectl --kubeconfig=${KUBECONFIG} create namespace taxi-services --dry-run=client -o yaml | kubectl --kubeconfig=${KUBECONFIG} apply -f -
                        
                        # Deploy MySQL first
                        echo "📦 Deploying MySQL..."
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services apply -f k8s/mysql-deployment.yaml
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services apply -f k8s/mysql-service.yaml
                        
                        # Wait for MySQL
                        echo "⏳ Waiting for MySQL to be ready..."
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services wait --for=condition=ready pod -l app=mysql --timeout=180s || {
                            echo "❌ MySQL failed to start!"
                            kubectl --kubeconfig=${KUBECONFIG} -n taxi-services describe pod -l app=mysql
                            exit 1
                        }
                        
                        # Deploy Backend
                        echo "📦 Deploying Backend..."
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services apply -f k8s/service.yaml
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services apply -f k8s/deployment.yaml
                        
                        # Update image
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services \
                            set image deployment/taxi-backend \
                            taxi-backend=${IMAGE_NAME}:${IMAGE_TAG}
                        
                        # Wait for rollout
                        echo "⏳ Waiting for rollout to complete..."
                        if ! kubectl --kubeconfig=${KUBECONFIG} -n taxi-services \
                            rollout status deployment/taxi-backend --timeout=300s; then
                            echo "❌ Deployment failed! Rolling back..."
                            kubectl --kubeconfig=${KUBECONFIG} -n taxi-services rollout undo deployment/taxi-backend
                            echo "Failed pods:"
                            kubectl --kubeconfig=${KUBECONFIG} -n taxi-services get pods -l app=taxi-backend
                            echo "Pod logs:"
                            kubectl --kubeconfig=${KUBECONFIG} -n taxi-services logs -l app=taxi-backend --tail=50
                            exit 1
                        fi
                        
                        echo "✅ Deployment successful!"
                    '''
                }
            }
        }
        
        stage('Verification') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig-file', variable: 'KUBECONFIG')]) {
                    sh '''
                        echo "========================================="
                        echo "DEPLOYMENT VERIFICATION"
                        echo "========================================="
                        
                        echo -e "\\n📦 ALL PODS:"
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services get pods -o wide
                        
                        echo -e "\\n🌐 SERVICES:"
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services get svc
                        
                        echo -e "\\n📊 DEPLOYMENTS:"
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services get deployments
                        
                        echo -e "\\n📋 BACKEND LOGS (last 20 lines):"
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services logs -l app=taxi-backend --tail=20 --prefix=true
                        
                        echo -e "\\n💾 MYSQL LOGS (last 10 lines):"
                        kubectl --kubeconfig=${KUBECONFIG} -n taxi-services logs -l app=mysql --tail=10
                    '''
                }
            }
        }
    }
    
    post {
        success {
            emailext(
                subject: "✅ Pipeline Success: ${env.JOB_NAME} - Build #${BUILD_NUMBER}",
                body: """
                    <h2>Deployment Successful!</h2>
                    <p><b>Application:</b> Rudra Banna Taxi Services</p>
                    <p><b>Image:</b> ${IMAGE_NAME}:${IMAGE_TAG}</p>
                    <p><b>Build Duration:</b> ${currentBuild.durationString}</p>
                    <p><b>Build URL:</b> ${env.BUILD_URL}</p>
                """,
                to: 'team@example.com'
            )
            echo '🎉 Pipeline Completed Successfully!'
        }
        failure {
            emailext(
                subject: "❌ Pipeline Failed: ${env.JOB_NAME} - Build #${BUILD_NUMBER}",
                body: """
                    <h2>Deployment Failed!</h2>
                    <p><b>Application:</b> Rudra Banna Taxi Services</p>
                    <p><b>Failed Stage:</b> ${env.STAGE_NAME}</p>
                    <p><b>Build URL:</b> ${env.BUILD_URL}</p>
                    <p>Please check logs for details.</p>
                """,
                to: 'team@example.com'
            )
            echo '❌ Pipeline Failed!'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs(
                cleanWhenNotBuilt: false,
                deleteDirs: true,
                disableDeferredWipeout: true
            )
        }
    }
}
