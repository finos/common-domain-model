#!/usr/bin/env bash

red=$(tput setaf 1)
azul=$(tput setaf 44)
reset=$(tput sgr0)

## make sure it is run with proper directory
bin=$(dirname "$0")
bin=$(cd "$bin"; pwd)

PORT=8659
printf "Preparing Docker image...\n"
docker build -t nginx-sphinx-docs .
if [ $? -ne 0 ]
then
  printf "Failed to build Docker image. Exiting... \n"
else
  printf "Docker image built!\n"
  STAMP=$(date +%d-%m-%Y-%H-%M-%S)
  printf "Running Docker image with timestamp $STAMP... Go to http://localhost:$PORT to check the site! \n"
  DOCKER_ID=$(docker run --rm --name "rosetta-doc-site-$STAMP" -p $PORT:80 nginx-sphinx-docs)
  if [ $? -ne 0 ]
  then
    printf "Could not run Docker image! \n"
  else
    printf "That's all folks!"
  fi
fi
