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


     stage('Run Unit Tests') {
                steps {
                                          git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                                       }
            }

stage('Code Quality Analysis') {
            steps {
                                                      git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                                                   }
        }


stage('Build Docker Image') {
            steps {
                 git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
              }
        }


 stage('Trivy Docker Image Scan') {
           steps {
                            git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                         }
        }

        stage('ZAP Security Scan') {
           steps {
                            git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                         }
        }



 stage('Push Docker Image to Registry') {
            steps {
                      git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                    }
        }

        stage('Deploy Application') {
                   steps {
                       git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
                 }
            }


  }

}
