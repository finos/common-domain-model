# Standard build:
#   docker build -t rosetta-python-gen -f python/build-resources/Dockerfile .
#   docker run --rm rosetta-python-gen                      # version defaults to 0.0.0
#   docker run --rm rosetta-python-gen 5.99.99              # pass a version number
#
# To run with generated output saved to the host:
#   docker run --name cdm-gen rosetta-python-gen 5.99.99; docker cp cdm-gen:/mnt/common-domain-model/rosetta-source/src/generated/python rosetta-source/src/generated/; docker rm cdm-gen
#
# To mount the docker environment and explore:
#   docker run --rm -it --entrypoint /bin/bash rosetta-python-gen

# To use a local generator JAR, set GENERATOR_JAR_FILE to its path inside the container (mount it first):
#   docker run --rm -v /path/to/python-generator.jar:/tmp/python-generator.jar -e GENERATOR_JAR_FILE=/tmp/python-generator.jar rosetta-python-gen
# If GENERATOR_JAR_FILE is unset or the file is not found, the script falls back to pulling from GitHub Releases.


FROM maven:3.9.9-eclipse-temurin-21-alpine

# Set working directory to /mnt (where the repo will be copied)
WORKDIR /mnt

# Install required tools
RUN apk add --no-cache python3 py3-pip bash git

# Copy the entire repo into /mnt
COPY . /mnt/common-domain-model/

WORKDIR /mnt/common-domain-model/

# Make the entrypoint script executable
RUN chmod +x python/build-resources/build-cdm-python.sh

# Set the entrypoint
ENTRYPOINT ["python/build-resources/build-cdm-python.sh"]