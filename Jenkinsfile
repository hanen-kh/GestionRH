pipeline {

  agent any
  stages{
      stage('git checkout'){
         steps{
            git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
         }

      }
      stage('unit tests') {
                  steps {
                      sh 'echo "Testing..."'
                      sh 'mvn test'
                  }
              }


  }

}