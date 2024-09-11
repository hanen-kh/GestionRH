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



      stage("Publish to Nexus") {
          steps {
              script {
                  // Lire le fichier POM en utilisant le plugin pipeline-utility-steps
                  pom = readMavenPom file: "pom.xml";

                  // Trouver l'artifact généré dans le dossier target
                  filesByGlob = findFiles(glob: "target/*.${pom.packaging}");

                  // Vérifier et afficher des informations sur l'artifact trouvé
                  echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}";

                  artifactPath = filesByGlob[0].path;
                  artifactExists = fileExists artifactPath;

                  if (artifactExists) {
                      echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version: ${pom.version}";

                      // Télécharger l'artifact et le fichier POM vers Nexus
                      nexusArtifactUploader(
                          nexusVersion: 'nexus3', // Version de Nexus
                          protocol: 'http', // Protocole de Nexus (http ou https)
                          nexusUrl: 'localhost:5/', // URL de Nexus
                          groupId: pom.groupId,
                          version: pom.version,
                          repository: 'GestionRH', // Remplacez par le nom de repository
                          credentialsId: 'Nexus', // Remplacez par l'ID des credentials Nexus dans Jenkins
                          artifacts: [
                              // Artifact généré ( .jar, .ear, .war)
                              [artifactId: pom.artifactId,
                              classifier: '',
                              file: artifactPath,
                              type: pom.packaging],

                              // Télécharger le fichier pom.xml pour les dépendances transitives
                              [artifactId: pom.artifactId,
                              classifier: '',
                              file: "pom.xml",
                              type: "pom"]
                          ]
                      );

                  } else {
                      error "*** File: ${artifactPath}, could not be found";
                  }
              }
          }
      }


  }

}