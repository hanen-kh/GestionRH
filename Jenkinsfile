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
                               sh '/usr/share/maven/bin/mvn clean package'

                          }
          }














  }

}
