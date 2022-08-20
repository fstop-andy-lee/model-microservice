# URL
  http://localhost:8080/swagger-ui.html
  http://localhost:8080/masters
  http://localhost:8080/log0s
  http://localhost:8080/log1s
  http://localhost:8080/log2s
  http://localhost:8080/api/saga
  http://localhost:8080/api/saga?testCase=1
  http://localhost:8080/api/saga?testCase=2

# Test
curl -v -i -X POST -H "Content-Type:application/json" -d '{  "id" : "Test", "balance" : 0 }' http://localhost:8080/masters


# install camunda spring starter

custom camunda spring starter artifact install
```
mvn install:install-file -Dfile=spring-zeebe-8.0.6-SNAPSHOT.jar -DgroupId=io.camunda -DartifactId=spring-zeebe -Dversion=8.0.6-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=spring-zeebe-starter-8.0.6-SNAPSHOT.jar -DgroupId=io.camunda -DartifactId=spring-zeebe-starter -Dversion=8.0.6-SNAPSHOT -Dpackaging=jar
```

# camunda

## tasklist

URL: http://localhost:8082/login
     demo / demo

## Operate

URL: http://localhost:8081/
     demo / demo

##



