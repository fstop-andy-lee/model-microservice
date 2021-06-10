pipeline {
    agent {
     label "jenkins=slave"
    }
	
    tools
    {
       maven "Maven3"
    }
    environment {
	  DOCKERHUB_TOKEN = credentials('dockerhub-token')
	  
	}
    stages {
      stage('Checkout Source') {
           steps {             
                git branch: 'develop', url: 'https://github.com/fstop-andy-lee/model-microservice.git'        
          }
        }
	stage('Execute Maven') {
	       environment {
                VERSION = sh(script:'cd ./swift-messaging && mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
           }
           steps {  
                sh 'mvn clean package -DskipTests'             
                sh 'rm ./swift-messaging/src/environment/docker/lib/dummy'
                sh 'cp ./swift-messaging/target/swift-messaging-${VERSION}.jar ./swift-messaging/src/environment/docker/lib/.'
                sh 'cp ./swift-messaging/src/main/resources/*.yml ./swift-messaging/src/environment/docker/conf/.'
                sh 'whoami'
                sh 'env'
          }
        }
    stage('Docker Build and Tag') {
           environment {
                VERSION = sh(script:'cd ./swift-messaging && mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
           }
           steps {  
                sh 'docker build --build-arg JAR_NAME=swift-messaging-${VERSION}.jar -t andylee1973/swift-messaging swift-messaging/src/environment/docker'   
                sh 'docker tag andylee1973/swift-messaging andylee1973/swift-messaging:latest'
          }
        }
     
    stage('Publish image to Docker Hub') {
           steps {
               withDockerRegistry([ credentialsId: "dockerhub-credential", url: "" ]) {
                 sh  'docker push andylee1973/swift-messaging'
                 sh  'docker logout'
              }                  
          }
        }     
    }
}
