#!/bin/bash

BUILD_HOME=/home/andy/work/git
GIT_URL=https://github.com/fstop-andy-lee/model-microservice.git
GIT_BRANCH=develop
PRJ_HOME=model-microservice
IMG_REPO_ACCT=andylee1973

CWD=`pwd`

mkdir -p $BUILD_HOME

cd $BUILD_HOME

git clone $GIT_URL

cd $PRJ_HOME

git checkout $GIT_BRANCH

mvn clean package -DskipTests

build_docker_image () {
SUB_PRJ=$1
IMAGE_NAME=${IMG_REPO_ACCT}/${SUB_PRJ}
SWD=`pwd`
cd $SUB_PRJ

#PRJ_VER=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout)
PRJ_VER=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo $PRJ_VER

rm ${BUILD_HOME}/${PRJ_HOME}/${SUB_PRJ}/src/environment/docker/lib/dummy > /dev/null 2>&1
cp ${BUILD_HOME}/${PRJ_HOME}/${SUB_PRJ}/target/${SUB_PRJ}-${PRJ_VER}.jar ${BUILD_HOME}/${PRJ_HOME}/${SUB_PRJ}/src/environment/docker/lib/.
cp ${BUILD_HOME}/${PRJ_HOME}/${SUB_PRJ}/src/main/resources/*.yml ${BUILD_HOME}/${PRJ_HOME}/${SUB_PRJ}/src/environment/docker/conf/.

sudo docker build --build-arg JAR_NAME=${SUB_PRJ}-${PRJ_VER}.jar -t ${IMAGE_NAME} ${BUILD_HOME}/${PRJ_HOME}/${SUB_PRJ}/src/environment/docker
sudo docker tag ${IMAGE_NAME} ${IMAGE_NAME}:latest
sudo docker push ${IMAGE_NAME}
cd $SWD
} 


# swift-messaging
build_docker_image swift-messaging

# inward-rmt-service
build_docker_image inward-rmt-service

# aml-service
build_docker_image aml-service


