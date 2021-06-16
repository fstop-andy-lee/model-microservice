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
	  IMAGE_NAME = 'andylee1973/swift-messaging'
	  
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
                sh 'docker build --build-arg JAR_NAME=swift-messaging-${VERSION}.jar -t ${IMAGE_NAME} swift-messaging/src/environment/docker'   
                sh 'docker tag ${IMAGE_NAME} ${IMAGE_NAME}:latest'
          }
        }
     
    stage('Publish image to Docker Hub') {
           steps {
               withDockerRegistry([ credentialsId: "dockerhub-credential", url: "" ]) {
                 sh  'docker push ${IMAGE_NAME}'
                 sh  'docker logout'
              }                  
          }
        }     

    stage('Deploy to k8s') {
           steps {
              withKubeConfig([credentialsId: 'k8s-api-key']) {
                sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
                sh 'chmod u+x ./kubectl'  
                sh './kubectl get pods'
                sh './kubectl patch  deployment swift-messaging --patch \'{\"spec\":{\"template\": {\"metadata\": {\"creationTimestamp\": \"2021-06-07T09:39:33Z\" }}}}\' '
              }
               
           }
        }
        
    }
}
