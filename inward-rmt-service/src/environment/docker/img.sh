#!/bin/bash

DCR_ACCT=andylee
LOCAL_DIR=/home/andy/work/inward-rmt-service
PGM_NAME=inward-rmt-service
PGM_VER=1.0.0

TAG_NAME=$PGM_NAME
APP_HOME=/home/spring/app


####
buildImage() {
  docker build -t $TAG_NAME --build-arg JAR_NAME=$PGM_NAME-$PGM_VER.jar .
}

run() {
  docker run -d --name $TAG_NAME -p 8080:8080 \
   -v $LOCAL_DIR/logs:$APP_HOME/logs \
   -e JAVA_OPTS='-Xmx1g -Xms128m -Djava.security.egd=file:/dev/urandom' \
   $TAG_NAME \
   --spring.config.location=$APP_HOME/conf/ \
   --server.port=8080 \
   --spring.profiles.active=prod

}

devRun() {
  docker run --rm --name $TAG_NAME -p 8080:8080 -p 9999:9999 \
   -v $LOCAL_DIR/logs:$APP_HOME/logs \
   -e JAVA_OPTS='-Xmx1g -Xms128m -Djava.security.egd=file:/dev/urandom -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9999' \
   $TAG_NAME \
   --spring.config.location=$APP_HOME/conf/ \
   --server.port=8080 \
   --spring.profiles.active=develop
}

toDocker() {
  docker exec -it $TAG_NAME sh 
}

pushImage() {
  docker tag $TAG_NAME $DCR_ACCT/$TAG_NAME
  docker push $DCR_ACCT/$TAG_NAME
}

##### main
case "$1" in
to-docker)
        toDocker
        ;;

build-image)
        buildImage
        ;;

run)
        run
        ;;

dev-run)
        devRun
        ;;

push-image)
        pushImage
        ;;
		
*)
  echo $"Usage: $0 {build-image|run|to-docker|push-image|dev-run}"
  exit 1
esac

