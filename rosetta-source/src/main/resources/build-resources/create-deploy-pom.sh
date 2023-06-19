#!/bin/bash
ARTIFACT_ID=$1
RELEASE_NAME=$2
PACKAGING=$3

FILENAME=${ARTIFACT_ID}-${RELEASE_NAME}.pom
GROUP_ID=org.finos.cdm
VERSION=${RELEASE_NAME}

cat > ${FILENAME} <<EOF
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>${GROUP_ID}</groupId>
    <artifactId>${ARTIFACT_ID}</artifactId>
    <version>${VERSION}</version>
    <packaging>${PACKAGING}</packaging>
    <scm>
        <developerConnection>scm:git:https://github.com/finos/common-domain-model</developerConnection>
        <tag>HEAD</tag>
    </scm>
</project>
EOF
