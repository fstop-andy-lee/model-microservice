#!/bin/bash

cd /opt/service2
java \
    -cp "conf:lib/*" \
    -Dspring.application.admin.enabled=false \
    -Dfile.encoding=UTF8 \
    -Djava.awt.headless=true \
	-Djava.security.egd=file:/dev/urandom \
    -Dlogger.file=conf/logback.xml \
	-Dloader.main=tw.com.firstbank.Service2App \
    -Dspring.profiles.active=prod \
    -Xms256m \
    -Xmx1024m \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/tmp \
	org.springframework.boot.loader.PropertiesLauncher
    
	