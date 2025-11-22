import os
import requests
import json 
from requests.auth import HTTPBasicAuth

backend_api_url = "http://localhost:8080/movie"
filename = os.path.join(os.path.expanduser("~"), "Workspace", "neondump", "movies.json")
moviePosterDestination = os.path.join(os.path.expanduser("~"), "Workspace", "moviePosters")

with open(filename, 'r') as f:
    data = json.load(f)
    for movie in data:
        posterfile_path = os.path.join(moviePosterDestination, movie["poster"])
        
        response = requests.post(backend_api_url, files=[
            ('file', (movie["poster"], open(posterfile_path, 'rb'), 'image/' + movie["poster"].split(".")[-1])),
            ('movieData', ('movieData', json.dumps(movie), 'application/json'))
        ], auth=HTTPBasicAuth('', ''))

        print(response)