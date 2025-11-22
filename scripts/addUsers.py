import requests
import os
import json

backend_api_url = "http://localhost:8080/register"
filename = os.path.join(os.path.expanduser("~"), "Workspace", "neondump", "users.json")

with open(filename, 'r') as f:
    data = json.load(f)
    for user in data:
        response = requests.post(backend_api_url, json=user)
        print(response.status_code)
        print("")