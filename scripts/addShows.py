import requests
import os
import json
from collections import namedtuple
from requests.auth import HTTPBasicAuth

backend_api_url = "http://localhost:8080/show"

neondumpfolder = os.path.join(os.path.expanduser("~"), "Workspace", "neondump")
theatreFile = os.path.join(neondumpfolder, "theatres.json")

theatreOwnership = dict()
with open(theatreFile, 'r') as f:
    data = json.load(f)
    for theatre in data:
        try:
            theatreOwnership[theatre["owner"]].append(theatre["id"])
        except KeyError:
            theatreOwnership[theatre["owner"]] = [theatre["id"]]

users = []
User = namedtuple('user', ['username', 'password', 'id'])
usersFile = os.path.join(neondumpfolder, "users.json")
with open(usersFile, 'r') as f:
    data = json.load(f)
    for user in data:
        users.append(User(username=user["username"], password=user["password"], id=user["id"]))


def getAuth(theatreId):
    for owner in theatreOwnership.keys():
        if theatreId in theatreOwnership[owner]:
            for user in users:
                if owner == user.id:
                    return user


showsFiles = ["shows_owner10_new.json"  ,
              "shows_owner4_new.json"  ,
              "shows_owner6_new.json"  ,
              "shows_owner8_new.json"]
for showFile in showsFiles:
    showFilePath = os.path.join(neondumpfolder, "shows", showFile)
    with open(showFilePath, 'r') as s:
        shows = json.load(s)
        for show in shows:
            theatreId = show["show"]["theatre"]["id"]
            user = getAuth(theatreId)
            response = requests.post(backend_api_url, json=show, auth=HTTPBasicAuth(user.username, user.password))
            print(response)

