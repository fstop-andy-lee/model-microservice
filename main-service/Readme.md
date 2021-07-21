# URL
  http://localhost:8080/masters
  http://localhost:8080/log0s
  http://localhost:8080/log1s
  http://localhost:8080/log2s
  http://localhost:8080/api/saga
  http://localhost:8080/api/saga?testCase=1
  http://localhost:8080/api/saga?testCase=2

# Test
curl -v -i -X POST -H "Content-Type:application/json" -d '{  "id" : "Test", "balance" : 0 }' http://localhost:8080/masters
