pipeline {

  agent any
  stages {
      stage('git checkout') {
         steps {
            git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
         }
      }

      stage('build') {
          steps {
              bat 'mvn clean package'
          }
      }

      stage('Unit Tests') {
          steps {
              bat 'mvn test'
          }
      }

      stage('SonarQube Analysis') {
          steps {
              withSonarQubeEnv(installationName: 'sonar', credentialsId: 'b6c852b6-4f44-4d8b-bcfb-09f690d90782') {
                  bat 'mvn clean package sonar:sonar'
              }
          }
      }

      stage('Quality Gate status') {
          steps {
              script {
                  waitForQualityGate abortPipeline: true, credentialsId: 'b6c852b6-4f44-4d8b-bcfb-09f690d90782'
              }
          }
      }



      stage('Docker image build') {
          steps {
              script {
                  // Construire l'image Docker
                  bat 'docker build -t khmilet/gestionrh:latest .'

                  // Stopper et supprimer l'ancien conteneur (s'il existe)
                  bat 'docker rm -f gestionrh-container || true'
              }
          }
      }



  }

}
