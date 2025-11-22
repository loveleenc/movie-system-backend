import json
import os
import requests
import re

omdb_poster_api_url = "https://www.omdbapi.com/?apikey=1d3587cb&t="

filename = os.path.join(os.path.expanduser("~"), "Workspace", "neondump", "movies.json")
moviePosterDestination = os.path.join(os.path.expanduser("~"), "Workspace", "moviePosters")

movieNameAndPoster = dict()

with open(filename, 'r') as f:
    data = json.load(f)
    for movie in data:
        name = movie["name"]
        response = requests.get(omdb_poster_api_url + name)
        print(response)
        try:
            poster_link = response.json()["Poster"]
        except KeyError:
            print("Movie not found: " + name)
            continue
        if poster_link == "N/A":
                print("WARNING: UNABLE TO FETCH poster name for movie: " + movie["name"])
                continue
        posterMatch = re.search(r'[^/]+/?$', poster_link)
        if posterMatch is not None:
            posterName = posterMatch.group(0)
            poster_response = requests.get(poster_link, stream=True)
            with open(os.path.join(moviePosterDestination, posterName), 'wb') as fd:
                for chunk in poster_response.iter_content(chunk_size=128):
                    fd.write(chunk)
            movieNameAndPoster[name] = posterName
        else:
            print("WARNING: UNABLE TO FETCH poster name for movie: " + movie["name"])

print(movieNameAndPoster)
