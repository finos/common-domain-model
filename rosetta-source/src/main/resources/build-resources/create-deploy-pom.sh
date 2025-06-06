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

    <name>${ARTIFACT_ID}</name>

    <url>https://www.finos.org/common-domain-model</url>

    <scm>
        <developerConnection>scm:git:https://github.com/finos/common-domain-model</developerConnection>
        <connection>scm:git:git://github.com/finos/common-domain-model.git</connection>
        <tag>HEAD</tag>
        <url>https://github.com/finos/common-domain-model</url>
    </scm>

    <description>The FINOS Common Domain Model (CDM) is a standardised, machine-readable and machine-executable blueprint for how financial products are traded and managed across the transaction lifecycle. It is represented as a domain model and distributed in open source.</description>

    <organization>
        <name>FINOS</name>
        <url>https://finos.org</url>
    </organization>

    <licenses>
        <license>
            <name>Community Specification License 1.0</name>
            <url>https://github.com/finos/common-domain-model/blob/master/LICENSE.md</url>
        </license>
    </licenses>

    <developers>
        <developer>
            <id>minesh-s-patel</id>
            <name>Minesh Patel</name>
            <email>infra@regnosys.com</email>
            <url>http://github.com/minesh-s-patel</url>
            <organization>REGnosys</organization>
            <organizationUrl>https://regnosys.com</organizationUrl>
            <timezone>+1</timezone>
            <roles>
                <role>Maintainer</role>
                <role>Developer</role>
            </roles>
        </developer>
        <developer>
            <id>hugohills-regnosys</id>
            <name>Hugo Hills</name>
            <email>infra@regnosys.com</email>
            <url>http://github.com/hugohills-regnosys</url>
            <organization>REGnosys</organization>
            <organizationUrl>https://regnosys.com</organizationUrl>
            <timezone>+1</timezone>
            <roles>
                <role>Maintainer</role>
                <role>Developer</role>
            </roles>
        </developer>
    </developers>
</project>
EOF
