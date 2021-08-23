FROM openjdk:8-jdk-alpine
EXPOSE 8070
EXPOSE 9999

# non root user
ARG USER_NAME=spring
RUN addgroup -S $USER_NAME && adduser -S $USER_NAME -G $USER_NAME
USER $USER_NAME:$USER_NAME

# build args are cleared after each FROM instruction!
# docker build -t service-gateway --build-arg JAR_NAME=service-gateway-1.0.0.jar .
ARG JAR_NAME=app.jar

# docker run -e JAVA_OPTS='-Xmx1g -Xms128m' xxx
# -Djava.security.egd=file:/dev/./urandom
# -Xmx1g -Xms512m
ENV JAVA_OPTS=""

#Set app home folder
ENV APP_HOME /home/$USER_NAME/app
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/lib
RUN mkdir $APP_HOME/conf
RUN mkdir $APP_HOME/logs

VOLUME $APP_HOME/lib
VOLUME $APP_HOME/conf
VOLUME $APP_HOME/logs

COPY ./lib/$JAR_NAME $APP_HOME/lib/app.jar
COPY ./conf/*.yml    $APP_HOME/conf/
COPY ./conf/*.xml    $APP_HOME/conf/

# The WORKDIR instruction sets the working directory for any RUN, CMD, ENTRYPOINT, COPY and ADD that follow it!
WORKDIR $APP_HOME

# enable remote debug from port 9999
# -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9999
ENTRYPOINT exec java -cp $APP_HOME/conf:$APP_HOME/lib/* $JAVA_OPTS -jar $APP_HOME/lib/app.jar $0 $@
