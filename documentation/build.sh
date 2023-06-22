#!/usr/bin/env bash

################################################################################
################################################################################
###### Useful script to test the doc creation process *locally*
###### Branch is specified as a parameter (or master if none passed)
######
###### Assumed installations:
######  1) docker (must also be up and running)
######  2) jq
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
mkdir build/releases
mkdir build/source

## root files
cp *.rst build

## cdm
cp -r source/* build/source

## sphynx
cp -r site/* build

printf "Creating release information... \n"

# export CORE_URL="http://localhost:5846"
export CORE_URL="https://ui.rosetta-technology.io"

printf "Retrieving latest release info... \n"

export LATEST_URL=${CORE_URL}/api/scm/releases/latest

echo "... get latest url is $LATEST_URL"

curl -sX GET "$LATEST_URL" > build/releases/latest.json
jq ' .bodyAsHtml' build/releases/latest.json > build/releases/latest.txt

sed -i 's/^.//;s/.$//' build/releases/latest.txt
sed -i 's/\\\"/"/g' build/releases/latest.txt
sed -i 's/\\n//g' build/releases/latest.txt

cat build/releases/latest.txt
cp build/releases/latest.txt build/releases/latest.html
printf "Retrieving all release info... \n"

export ALL_URL=${CORE_URL}/api/scm/releases/all
echo "... get all url is $ALL_URL"
curl -sX GET "$ALL_URL" > build/releases/all.json
jq  '.[] | "<h4>" + .tag + "</h4>", .bodyAsHtml' build/releases/all.json > build/releases/all.txt
sed -i 's/^.//;s/.$//' build/releases/all.txt
sed -i 's/\\\"/"/g' build/releases/all.txt
sed -i 's/\\n//g' build/releases/all.txt
cp build/releases/all.txt build/releases/all.html

export LATEST_RELEASE_ID=$(jq ' .tag' build/releases/latest.json | rev | cut -c2- | rev | cut -c2-)
envsubst \$LATEST_RELEASE_ID < latest-template.rst >  build/releases/latest.rst
envsubst \$LATEST_RELEASE_ID < source/links-template.rst >  build/source/links.rst
cp all.rst build/releases/all.rst
