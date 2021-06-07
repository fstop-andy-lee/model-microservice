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
      stage('checkout') {
           steps {             
                git branch: 'develop', url: 'https://github.com/fstop-andy-lee/model-microservice.git'             
          }
        }
	  stage('Execute Maven') {
           steps {             
                sh 'mvn clean package -DskipTests'             
                sh 'rm ./swift-messaging/src/environment/docker/lib/dummy'
                sh 'cp ./swift-messaging/target/swift-messaging-1.0.0.jar ./swift-messaging/src/environment/docker/lib/.'
                sh 'cp ./swift-messaging/src/main/resources/*.yml ./swift-messaging/src/environment/docker/conf/.'
          }
        }        
      stage('Docker Build and Tag') {
           steps {              
                sh 'docker build --build-arg JAR_NAME=swift-messaging-1.0.0.jar -t andylee1973/swift-messaging swift-messaging/src/environment/docker'   
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
