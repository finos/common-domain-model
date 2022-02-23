#!/usr/bin/env bash

################################################################################
################################################################################
###### Useful script to test the doc creation process *locally*
###### Branch is specified as a parameter (or master if none passed)
######
###### Assumed installations:
######  1) docker (must also be up and running)
######  2) gh (github CLI)
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
rm -rf build
mkdir build

## root files
cp ../../*.rst build
cp ../*.rst build

## cdm
mkdir build/docs
cp -r ../source/* build/docs

## sphynx
cp -r _static build
cp -r _templates build
cp -r conf.py build

printf "Creating release information... \n"
mkdir build/releases

# LATEST_RELEASE=$(gh release list -L 1 | awk '{print $1}' | xargs)
# LATEST_RELEASE_NOTE=$(gh release view $LATEST_RELEASE --json body --jq '.body')
#
# echo "# Latest release $LATEST_RELEASE" > build/releases/latest.md
# echo $LATEST_RELEASE_NOTE >> build/releases/latest.md
#
# RELEASE_LIST=$(gh release list -L 5 | awk '{print $1}' | xargs)
#
# for RELEASE in $RELEASE_LIST
#   do
#     echo $RELEASE
#     RELEASE_NOTES=$(gh release view $RELEASE --json body --jq '.body')
#
#     echo "# $RELEASE" >> build/releases/latest.md
#     echo $RELEASE_NOTES >> build/releases/latest.md
# #    echo $RELEASE_NOTES > build/releases/${RELEASE}.md
#   done

CORE_URL="http://localhost:5846"

# latest release
curl -sX GET "${CORE_URL}/api/scm/releases/latest" > build/releases/latest.json
LATEST_RELEASE_ID=$(jq ' .tag' build/releases/latest.json | rev | cut -c2- | rev | cut -c2-)
envsubst \$LATEST_RELEASE_ID < latest-template.rst >  build/releases/latest.rst
LATEST_EXTRACT=$(jq  '"<h4>" + .tag + "</h4>", .bodyAsHtml' build/releases/latest.json  | rev | cut -c2- | rev | cut -c2-)
echo ${LATEST_EXTRACT//\\n/} > build/releases/latest.html

# all releases
curl -sX GET "${CORE_URL}/api/scm/releases/all" > build/releases/all.json
ALL_CLEANED=$(jq  '"<h4>" + .tag + "</h4>", .bodyAsHtml' build/releases/all.json  | rev | cut -c2- | rev | cut -c2-)
echo ${ALL_CLEANED//\\n/} > build/releases/all.html

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
  DOCKER_ID=$(docker run --rm --name "rosetta-doc-site-$STAMP" -p $PORT:80 nginx-sphinx-docs)
  if [ $? -ne 0 ]
  then
    printf "Could not run Docker image! \n"
  else
    printf "Done! Go to http://localhost:$PORT to check the site! \n"
    printf "To stop - run: docker stop $DOCKER_ID"

  fi
fi
