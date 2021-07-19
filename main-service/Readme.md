# URL
  http://localhost:8080/masters

# Test
curl -v -i -X POST -H "Content-Type:application/json" -d '{  "id" : "Test", "balance" : 0 }' http://localhost:8080/masters
