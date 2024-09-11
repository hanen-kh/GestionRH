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

      stage('SonarQube Analysis')    {

                   steps{
                   withSonarQubeEnv(installationName: 'sonar', credentialsId: 'b6c852b6-4f44-4d8b-bcfb-09f690d90782') {
                              bat 'mvn clean package sonar:sonar'
                          }
                   }
                                     }

      stage('Quality Gate status'){

                        steps{

                          script{waitForQualityGate abortPipeline: true, credentialsId: 'b6c852b6-4f44-4d8b-bcfb-09f690d90782'}
                          }

                        }
       stage('upload to nexus'){
                        steps{
                          nexusArtifactUploader artifacts:
                          [
                            [
                               artifactId: 'GestionRH',
                                classifier: '',
                                file: 'target/GestionRH-0.0.1-SNAPSHOT.jar',
                                 type: 'jar'
                                 ]
                                 ],
                                  credentialsId: 'nexus',
                                   groupId: 'intern',
                                    nexusUrl: 'localhost:5',
                                     nexusVersion: 'nexus3',
                                      protocol: 'http',
                                       repository: 'GestionRH',
                                        version: '0.0.1'

                        }


                }
  }

}