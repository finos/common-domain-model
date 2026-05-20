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

## CDM as DAML

CDM as DAML is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-daml/versions). Then click on the `Browse` button for latest version available as `.tar.gz` file containing compiled `.dar` files.

## CDM as Scala

CDM as Scala is published 
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-scala/versions). Then click on the `Browse` button for latest version available as `.jar` file .

## CDM as C# 8.0

CDM as C# 8.0 is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-csharp8/versions). Then click on the `Browse` button for latest version available as a `tar.gz` file containing compiled `.dll` files.

## CDM as C# 9.0

CDM as C# 9.0 is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-csharp9/versions). Then click on the `Browse` button for latest version available as a `tar.gz` file containing compiled `.dll` files.

## CDM as Go

CDM as Go is files published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-golang/versions). Then click on the `Browse` button for latest version available as a `tar.gz` file containing the `.go`

## CDM as TypeScript

CDM as TypeScript is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-typescript/versions). Then click on the `Browse` button for latest version available as a `zip` file containing the `.ts` files.

## CDM as Kotlin

CDM as Kotlin is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-scala/versions). Then click on the `Browse` button for latest version available as a `.jar` file.

## CDM as Excel

CDM as Excel is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-excel/versions). Then click on the `Browse` button for latest version available as a `.xlsx` file containing CDM Types, Attributes and Enums.

## CDM as JSON Schema

CDM as JSON Schema is published
[here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-json-schema/versions). Then click on the `Browse` button for latest version available as a `zip` file containing the `.json` schema files.


## CDM Event Specification Module as DAML

CDM Event Specification Module as DAML is available
[here](https://github.com/digital-asset/lib-cdm-event-specification-module).