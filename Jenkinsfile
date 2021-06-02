agent {
    dockerfile {
        filename 'Dockerfile'
        dir 'swift-messaging/src/environment/docker'
        label 'latest'
        registryUrl 'https://hub.docker.com/'
        registryCredentialsId 'andy-lee-dockerhub-token'
        additionalBuildArgs  '--build-arg JAR_NAME=swift-messaging-1.0.0.jar'

    }
}
