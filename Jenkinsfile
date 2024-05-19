pipeline {
  agent any

  stages {

      stage('Deletion') {
            steps {
                echo '--Remove repository if it exists --'
                sh "rm -rf trade-store-service"
            }
        }
       stage('Clone') {
            steps {
               withCredentials([usernamePassword(credentialsId: 'GitHub-PAT', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {

                sh 'git config --global user.name "${GIT_USERNAME}"'
                sh 'git config --global user.password "${GIT_PASSWORD}"'

                echo '--Cloning Git Repo started ---'
                sh "git clone https://{GIT_USERNAME}:$GIT_PASSWORD@github.com/{GIT_USERNAME}/jenkins-maven.git"

                echo '--Cloning Git Repo ended ---'

                sh "pwd"
                sh  "ls -lrt"

               }
            }
        }

      stage('Build Artifact') {
            steps {

                dir('/var/jenkins_home/workspace/Java_Pipeline/jenkins-maven') {

                   echo '--Building artifact started ---'

                  sh "mvn clean package -DskipTests=true"
                  archive 'target/*.jar'

                  echo '--Building artifact ended ---'
                }
            }
       }
      stage('Test Maven - JUnit') {
            steps {
                     dir('/var/jenkins_home/workspace/Java_Pipeline/jenkins-maven') {
                     echo '--Run Unit Test started ---'

                         sh "mvn test"
                     echo '--Run Unit Test ended ---'
                     }
            }
            post{
              always{
                junit '**/target/surefire-reports/*.xml'
              }
            }
        }


      stage('Sonarqube Analysis - SAST') {
            steps {

                echo '--SonarQube Analysis ---'

           //       withSonarQubeEnv('SonarQube') {
           //            dir('/var/jenkins_home/workspace/Java_Pipeline/jenkins-maven') {
           //                     sh "mvn sonar:sonar \
            //                  -Dsonar.projectKey=maven-jenkins-pipeline \
            //            -Dsonar.host.url=http://localhost:9000"
              //         }
               // }
           //timeout(time: 2, unit: 'MINUTES') {
             //         script {
               //         waitForQualityGate abortPipeline: true
                 //   }
               // }
            //  }
        }

         stage('Upload Artifact ') {
            steps {
                echo '--Uploading artifact to repository ---'
                }
          }

           stage('Deploy Artifact ') {
              steps {
                  echo '--Download artifact from repository ---'
                   echo '--Script to deploy artifact to environment ---'
                  }
            }

     }
}
