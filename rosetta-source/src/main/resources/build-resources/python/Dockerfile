# docker build -t rosetta-python-gen -f rosetta-source/src/main/resources/build-resources/python/Dockerfile .
# docker run --rm rosetta-python-gen

# to run with results saved
# docker run --rm -v "$(pwd)":/mnt/common-domain-model -w /mnt/common-domain-model rosetta-python-gen

FROM maven:3.9.9-eclipse-temurin-21-alpine

# Set working directory to /mnt (where the repo will be copied)
WORKDIR /mnt

# Install required tools
RUN apk add --no-cache python3 py3-pip wget curl bash

# Copy the entire repo into /mnt
COPY . /mnt/common-domain-model/

WORKDIR /mnt/common-domain-model/

# Make the entrypoint script executable
RUN chmod +x rosetta-source/src/main/resources/build-resources/python/build-cdm-python.sh

# Set the entrypoint
ENTRYPOINT ["rosetta-source/src/main/resources/build-resources/python/build-cdm-python.sh"]