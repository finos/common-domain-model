#!/bin/bash
ARTIFACT_ID=$1
RELEASE_NAME=$2
PACKAGING=$3

GROUP_ID=org.finos.cdm
VERSION=${RELEASE_NAME}

cat > pom.xml <<EOF
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>${GROUP_ID}</groupId>
    <artifactId>${ARTIFACT_ID}</artifactId>
    <version>${VERSION}</version>
    <packaging>pom</packaging>

    <name>${ARTIFACT_ID}</name>

    <parent>
        <artifactId>cdm-parent</artifactId>
        <groupId>org.finos.cdm</groupId>
        <version>0.0.0.master-SNAPSHOT</version>
    </parent>

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

    <profiles>
        <profile>
            <id>release</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.codehaus.mojo</groupId>
                        <artifactId>build-helper-maven-plugin</artifactId>
                        <version>3.3.0</version>
                        <executions>
                            <execution>
                                <id>attach-artifacts</id>
                                <phase>package</phase>
                                <goals>
                                    <goal>attach-artifact</goal>
                                </goals>
                                <configuration>
                                    <artifacts>
                                        <artifact>
                                            <file>\${project.basedir}/${ARTIFACT_ID}-\${project.version}.${PACKAGING}</file>
                                            <type>${PACKAGING}</type>
                                        </artifact>
                                    </artifacts>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>

                    <plugin>
                        <groupId>org.sonatype.central</groupId>
                        <artifactId>central-publishing-maven-plugin</artifactId>
                        <version>0.7.0</version>
                        <executions>
                            <execution>
                                <id>publish-release</id>
                                <phase>deploy</phase>
                                <goals>
                                    <goal>publish</goal>
                                </goals>
                                <configuration>
                                    <autoPublish>false</autoPublish>
                                    <waitUntil>VALIDATED</waitUntil>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>
EOF
