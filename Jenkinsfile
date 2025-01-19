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
                              echo 'Building the project with Maven...'
                               bat'mvn clean package'
                          }
          }

          stage('Unit Tests') {
               steps {
                              echo 'Running unit tests...'
                               bat'mvn test'
                          }
          }












  }

}
