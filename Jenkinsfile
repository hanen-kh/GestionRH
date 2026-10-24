pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    tools {
        maven 'Maven-3.9'   
        jdk   'JDK-17'      
    }

    environment {
        IMAGE_NAME      = 'khmilett/devsecops'
        IMAGE_TAG       = "${BUILD_NUMBER}"
        FULL_IMAGE      = "${IMAGE_NAME}:${IMAGE_TAG}"
        TRIVY_SEVERITY  = 'HIGH,CRITICAL'
        DOCKERHUB_CREDS = credentials('dockerhub-credentials')
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build & Unit Tests') {
            steps {
                sh 'mvn -B clean verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis (SAST)') {
            steps {
                withSonarQubeEnv('sonarqube') {   
                    sh 'mvn -B sonar:sonar -Dsonar.projectKey=gestionrh'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Scan Source (deps + secrets)') {
            steps {
                sh '''
                trivy fs --scanners vuln,secret \
                    --severity ${TRIVY_SEVERITY} \
                    --ignore-unfixed --exit-code 0 .
                '''
            }
        }

        stage('Lint Dockerfile') {
            steps {
                sh 'hadolint Dockerfile || true'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${FULL_IMAGE} -t ${IMAGE_NAME}:latest .'
            }
        }

        stage('Scan Docker Image (Trivy)') {
            steps {
                sh '''
                trivy image --format json --output trivy-report.json ${FULL_IMAGE}
                trivy image --severity ${TRIVY_SEVERITY} \
                    --ignore-unfixed --exit-code 1 ${FULL_IMAGE}
                '''
            }
        }

        stage('Push to Registry') {
            when { branch 'main' }
            steps {
                sh '''
                echo "$DOCKERHUB_CREDS_PSW" | docker login -u "$DOCKERHUB_CREDS_USR" --password-stdin
                docker push ${FULL_IMAGE}
                docker push ${IMAGE_NAME}:latest
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'trivy-report.json, target/*.jar', allowEmptyArchive: true
            sh '''
            docker logout || true
            docker rmi ${FULL_IMAGE} ${IMAGE_NAME}:latest || true
            '''
            cleanWs()
        }
        success { echo 'Pipeline executed successfully!' }
        failure { echo 'Pipeline failed!' }
    }
}
