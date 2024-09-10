pipeline {

  agent any
  stages{
      stage('git checkout'){
         steps{
            git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
         }

      }
      stage('build') {
                  steps {

                      bat 'mvn clean package'
                  }
              }
      stage('Unit Tests'){
                    steps{
                    bat 'mvn test'
                   }


  }
  }

}