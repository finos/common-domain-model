#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

export CDM_ROSETTA="rosetta-source/src/main/rosetta"
export PYTHON_TARGET="rosetta-source/target/cdm-python"

# Extract and set ROSETTA_CODE_GEN_VERSION to the rosetta.dsl.version in the parent POM
export ROSETTA_CODE_GEN_VERSION=$(mvn help:evaluate -Dexpression=rosetta.dsl.version -q -DforceStdout)
echo "rosetta.code-gen.version: ${ROSETTA_CODE_GEN_VERSION}"

# Find the latest release tag that matches the DSL version (x.y.z.n)
REPO="finos/rune-python-generator"
DSL_VERSION="${ROSETTA_CODE_GEN_VERSION}"

echo "Looking for latest generator release matching DSL version: ${DSL_VERSION}"

LATEST_TAG=$(curl -s "https://api.github.com/repos/${REPO}/tags?per_page=100" \
  | grep '"name":' \
  | cut -d '"' -f 4 \
  | grep -E "^${DSL_VERSION}\.[0-9]+$" \
  | sort -t. -k4 -n \
  | tail -n 1)

if [[ -z "${LATEST_TAG}" ]]; then
  echo "ERROR: No generator release found for DSL version ${DSL_VERSION}"
  exit 1
fi

echo "Latest matching generator tag: ${LATEST_TAG}"

GENERATOR_JAR="python-${LATEST_TAG}.jar"
GENERATOR_URL="https://github.com/${REPO}/releases/download/${LATEST_TAG}/${GENERATOR_JAR}"
echo "Attempting to download ${GENERATOR_URL}"

if ! wget -q --spider "${GENERATOR_URL}"; then
  echo "ERROR: Generator jar ${GENERATOR_JAR} not found for tag ${LATEST_TAG}"
  exit 1
fi

wget -O "/tmp/${GENERATOR_JAR}" "${GENERATOR_URL}" || { echo "Failed to download generator JAR"; exit 1; }

# Run the generator
java -cp "/tmp/${GENERATOR_JAR}" com.regnosys.rosetta.generator.python.PythonCodeGeneratorCLI -s "${CDM_ROSETTA}" -t "${PYTHON_TARGET}"

export PYTHONDONTWRITEBYTECODE=1
python3 -m venv /tmp/.pyenv
source /tmp/.pyenv/bin/activate
python3 -m pip install --upgrade pip

cd "${PYTHON_TARGET}"

# Download the latest rune-python-runtime wheel from GitHub releases
export RUNTIME_WHEEL_URL=$(curl -s https://api.github.com/repos/finos/rune-python-runtime/releases/latest | grep browser_download_url | grep whl | cut -d '"' -f 4)
export RUNTIME_WHEEL_NAME=$(basename "${RUNTIME_WHEEL_URL}")
echo "Downloading latest runtime wheel: ${RUNTIME_WHEEL_NAME}"
wget -O "${RUNTIME_WHEEL_NAME}" "${RUNTIME_WHEEL_URL}" || { echo "Failed to download runtime wheel"; exit 1; }
python3 -m pip install "${RUNTIME_WHEEL_NAME}"

# Build and install the generated Python package
python3 -m pip wheel --no-deps --only-binary :all: --wheel-dir . .
WHEEL_FILE=$(ls ./python_cdm-*-py3-none-any.whl | head -n 1)
if [[ ! -f "${WHEEL_FILE}" ]]; then
  echo "Wheel file not found!"
  exit 1
fi
python3 -m pip install "${WHEEL_FILE}"
python3 -m pip install pytest

# Run unit tests (output will be visible in Docker logs)
pytest -p no:cacheprovider /mnt/common-domain-model/cdm-python/test/