# Building the Docker image:
#   docker build -t rosetta-python-gen -f python/Dockerfile .
# Stadard usage (output will be visible in Docker logs):
#   docker run --rm rosetta-python-gen                      # version defaults to 0.0.0
#   docker run --rm rosetta-python-gen 7.99.99              # pass a version number
#
# To run with generated output saved to the host:
#   docker run --name cdm-gen rosetta-python-gen <version number>; docker cp cdm-gen:/mnt/common-domain-model/rosetta-source/src/generated/python rosetta-source/src/generated/; docker rm cdm-gen
#      <version number> is optional and defaults to 0.0.0 if not provided. Examples include 7.0.0-dev.98, 7.99.99, etc.
#
#   docker run --name cdm-gen rosetta-python-gen 7.0.0-dev.98; docker cp cdm-gen:/mnt/common-domain-model/rosetta-source/src/generated/python rosetta-source/src/generated/; docker rm cdm-gen
#   docker run --name cdm-gen rosetta-python-gen 7.99.99; docker cp cdm-gen:/mnt/common-domain-model/rosetta-source/src/generated/python rosetta-source/src/generated/; docker rm cdm-gen
#
# To mount the docker environment and explore:
#   docker run --rm -it --entrypoint /bin/bash rosetta-python-gen

FROM maven:3.9.11-eclipse-temurin-21

# Set working directory to /mnt (where the repo will be copied)
WORKDIR /mnt

# Install required tools
RUN apt-get update -qq && apt-get install -y --no-install-recommends python3 python3-pip python3-venv git && rm -rf /var/lib/apt/lists/*

# Copy the entire repo into /mnt
COPY . /mnt/common-domain-model/

WORKDIR /mnt/common-domain-model/

# Make the entrypoint script executable
RUN chmod +x python/build-cdm-python.sh

# Set the entrypoint
ENTRYPOINT ["python/build-cdm-python.sh"]
