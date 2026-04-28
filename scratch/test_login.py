import http.client
import json

conn = http.client.HTTPConnection("localhost", 8080)
payload = json.dumps({"username": "admin", "password": "password"})
headers = {'Content-Type': 'application/json'}
conn.request("POST", "/api/auth/login", payload, headers)
res = conn.getresponse()
data = res.read()
print(f"Status: {res.status}")
print(f"Response: {data.decode('utf-8')}")
conn.close()
