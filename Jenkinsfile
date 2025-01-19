pipeline {

  agent any
  stages {
      stage('git checkout') {
         steps {
            git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
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
