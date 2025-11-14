#!/bin/bash

env_variables=("DB_URL" "AZURE_BLOB_STORAGE_URL" "AZURE_STORAGE_CONNECTION_STRING" "AZURE_BLOB_CONTAINER_NAME")

mkdir -p secrets/
for variable in ${env_variables[*]}
do
    var_name=${!variable}
    printf "$variable=$var_name\n"
done >> secrets/movieSecrets.properties

java -jar java-app.jar
