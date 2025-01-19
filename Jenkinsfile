pipeline {

  agent any
  stages {
      stage('git checkout') {
         steps {
            git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
         }
      }


stage('Build') {
            steps {
                        git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                     }
        }

        stage('Unit Tests') {
           steps {
                       git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                    }
        }

        stage('SonarQube Analysis') {
            steps {
                        git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                     }
            }
        }

        stage('OWASP ZAP Scan') {
            steps {
                        git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                     }
        }

        stage('Build Docker Image') {
            steps {
                        git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                     }
        }

        stage('Push Docker Image') {
            steps {
                        git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                     }
        }

        stage('Scan with Trivy') {
            steps {
                        git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                     }
        }

        stage('Deploy to Docker Container') {
           steps {
                       git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
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
            cleanWs()
        }
    }
}