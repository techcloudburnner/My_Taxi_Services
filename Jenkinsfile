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

                    docker logout
                    """
                }
            }
        }

        stage('Verify Image') {
            steps {
                sh """
                echo "Build Successful"
                echo "Image: $IMAGE_NAME:$IMAGE_TAG"
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
    }
}
