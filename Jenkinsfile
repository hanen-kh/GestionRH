pipeline {

  agent any
  stages{
      stage('git checkout'){
         steps{
            git branch: 'main', url: 'https://github.com/hanen-kh/GestionRH.git'
         }

      }
      stage('clean package') {
                  steps {

                      sh 'mvn clean package'
                  }
              }


  }

}