---
title: Download Source Code
---

The CDM source code is open source and can be downloaded from [CDM
GitHub](https://github.com/finos/common-domain-model). All released versions are tagged and can be downloaded from
[CDM GitHub Releases](https://github.com/finos/common-domain-model/releases).

## Versions

The CDM code is tagged using semantic versioning and is available on
[GitHub
Releases](https://github.com/finos/common-domain-model/releases). All
CDM Binary distributions are created on every release and are all
published to [Maven
Central](https://central.sonatype.com/artifact/org.finos.cdm/cdm-parent).

## CDM as Java

The CDM is distributed in Java and is available in [Maven
Central](https://central.sonatype.com).

### Setup

In order to use the CDM in a [Maven](https://maven.apache.org) project,
the following dependency needs to be added to the project pom.xml:

---
**dependency**

  
    <groupId>org.finos.cdm</groupId>   
    <artifactId>cdm-java</artifactId>   
    <version>LATEST</version>

> _NOTE:_ All current CDM dependencies are available in Maven Central.
> CDM releases prior to version 4.0.0 can be found in the [ISDA repository](https://europe-west1-maven.pkg.dev/production-208613/isda-maven).
> The dependencies of CDM releases prior to version 4.0.0 can be found in the [REGnosys repository](https://europe-west1-maven.pkg.dev/production-208613/public-maven).
> Add the following snippet to the `<repositories>` section of your project `pom.xml`:
>
> ``` xml
>    <repositories>
>        <repository>
>            <id>isda-maven</id>
>            <url>https://europe-west1-maven.pkg.dev/production-208613/isda-maven</url>
>            <releases>
>                <enabled>true</enabled>
>            </releases>
>            <snapshots>
>                <enabled>false</enabled>
>            </snapshots>
>        </repository>
>        <repository>
>            <id>public-maven</id>
>            <url>https://europe-west1-maven.pkg.dev/production-208613/public-maven</url>
>            <releases>
>                <enabled>true</enabled>
>            </releases>
>            <snapshots>
>                <enabled>false</enabled>
>            </snapshots>
>        </repository>
>        <!-- existing contents -->
>    </repositories>
>```

---

## CDM as Java Examples

Examples of how you can use
the CDM can be found in GitHub: [CDM GitHub Examples](https://github.com/finos/common-domain-model/tree/master/examples).

## CDM as Python

CDM as Python is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-python/versions). Then click on the `Browse` button for latest version available as `.tar.gz` file .

## CDM as TypeScript

CDM as TypeScript is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-typescript/versions). Then click on the `Browse` button for latest version available as a `zip` file containing the `.ts` files.

## CDM as Excel

CDM as Excel is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-excel/versions). Then click on the `Browse` button for latest version available as a `.xlsx` file containing CDM Types, Attributes and Enums.

## CDM as JSON Schema

CDM as JSON Schema is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-json-schema/versions). Then click on the `Browse` button for latest version available as a `zip` file containing the `.json` schema files.
