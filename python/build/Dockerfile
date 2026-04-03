# Standard build:
#   docker build -t rosetta-python-gen -f python/build/Dockerfile .
#   docker run --rm rosetta-python-gen
#
# To run with results saved:
#   docker run --rm -v "$(pwd)":/mnt/common-domain-model -w /mnt/common-domain-model rosetta-python-gen
#
# To test with a local generator JAR, mount it to /tmp/local-generator.jar and set USE_LOCAL_JAR_FLAG:
# docker run --rm -v /Users/dls/projects/rune/rune-python-generator/FINOS/rune-python-generator/target/python-0.0.0.main-SNAPSHOT.jar:/tmp/local-generator.jar -v "$(pwd)":/mnt/common-domain-model -w /mnt/common-domain-model -e USE_LOCAL_JAR_FLAG=1 rosetta-python-gen

FROM maven:3.9.9-eclipse-temurin-21-alpine

# Set working directory to /mnt (where the repo will be copied)
WORKDIR /mnt

# Install required tools
RUN apk add --no-cache python3 py3-pip wget curl bash git

# Copy the entire repo into /mnt
COPY . /mnt/common-domain-model/

WORKDIR /mnt/common-domain-model/

# Make the entrypoint script executable
RUN chmod +x python/build/build-cdm-python.sh

# Set the entrypoint
ENTRYPOINT ["python/build/build-cdm-python.sh"]