pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'devSecOps:latest'            // Nom et tag de l'image Docker
        DOCKER_REGISTRY = 'khmilett/devsecops'    // URL du registre Docker
        TRIVY_SEVERITY = 'HIGH,CRITICAL'          // Niveaux de gravité pour Trivy
    }

    stages {
        stage('Git Checkout') {
            steps {
                echo 'Cloning the repository...'
                git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh '''
                docker build -t ${DOCKER_IMAGE} .
                '''
            }
        }

        stage('Scan with Trivy') {
            steps {
                echo 'Scanning Docker image with Trivy...'
                sh '''
                trivy image --severity ${TRIVY_SEVERITY} ${DOCKER_IMAGE}
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            cleanWs() // Nettoie le workspace Jenkins après chaque exécution
        }
    }
}
