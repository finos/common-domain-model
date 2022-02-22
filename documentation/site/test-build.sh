#!/usr/bin/env bash

################################################################################
################################################################################
###### Useful script to test the doc creation process *locally*
###### Branch is specified as a parameter (or master if none passed)
######
###### Assumed installations:
######  1) docker (must also be up and running)
######  2) curl
######  3) jq
######  4) envsubst (gettext)
################################################################################
################################################################################

red=$(tput setaf 1)
azul=$(tput setaf 44)
reset=$(tput sgr0)

## make sure it is run with proper directory
bin=$(dirname "$0")
bin=$(cd "$bin"; pwd)

printf "Copying CDM artefacts... \n"

## Make a build dir and copy everythign that needs to built
mkdir build
cp ../../*.rst build
cp -r ../source/* build

printf "Creating release information... \n"
CORE_URL="http://localhost:5846"

## latest release
# curl -sX GET "${CORE_URL}/api/scm/releases/latest" > build/latest.json
# LATEST_RELEASE_ID=$(jq ' .tag' build/latest.json | rev | cut -c2- | rev | cut -c2-)
# envsubst \$LATEST_RELEASE_ID < cdm/releases/latest-template.rst >  cdm/releases/latest.rst
# LATEST_EXTRACT=$(jq  '"<h4>" + .tag + "</h4>", .bodyAsHtml' latest.json  | rev | cut -c2- | rev | cut -c2-)
# echo ${LATEST_EXTRACT//\\n/} > cdm/releases/latest.html

## all releases
# curl -sX GET "${CORE_URL}/api/scm/releases/all" > all.json
# echo ${ALL_CLEANED//\\n/} > cdm/releases/all.html

PORT=8659
printf "Preparing Docker image...\n"
docker build -t nginx-sphinx-docs .
if [ $? -ne 0 ]
then
  printf "Failed to build Docker image. Exiting... \n"
else
  printf "Docker image built!\n"
  STAMP=$(date +%d-%m-%Y-%H-%M-%S)
  printf "Running Docker image with timestamp $STAMP... \n"
  docker run --rm --name "rosetta-doc-site-$STAMP" -d -p $PORT:80 nginx-sphinx-docs
  if [ $? -ne 0 ]
  then
    printf "Could not run Docker image! \n"
  else
    printf "Done! Go to http://localhost:$PORT to check the site! \n"
  fi
fi
