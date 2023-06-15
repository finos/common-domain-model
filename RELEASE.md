# *Infrastructure - CDM Artifacts published to Maven Central*

#### _What is being released?_

As part of the CDM migration to FINOS, all generated artifacts are now being published to Maven Central. The intention to make this change was presented to CDM community, and noted in the CDM transition document ["Common Domain Model becoming Open Source @ FINOS"](https://assets.isda.org/media/6fac704a/081f5537.pdf), earlier this year. This is a positive step for the CDM community and realizes a demand from implementers that the project has noted for some time.

#### _Downloading Source Code_

Previously, only the latest CDM source code was available to download from the CDM Portal. After this release, the open source code is only available to be downloaded from [CDM GitHub](https://github.com/finos/common-domain-model). However, additional to the latest version, previously released versions are tagged and can be downloaded from [CDM GitHub Releases](https://github.com/finos/common-domain-model/releases) also.

#### _CDM as Java_

In order to use the latest version of CDM in a [Maven](https://maven.apache.org) project, the following dependency needs to be added to the project pom.xml:

```
<dependency>
  <groupId>org.finos.cdm</groupId>
  <artifactId>cdm-java</artifactId>
  <version>LATEST</version>
</dependency>
```

Going forward from this release, the "LATEST" value can be substituted for with any of the subqequent [CDM GitHub Releases](https://github.com/finos/common-domain-model/releases). 

For implemntation of CDM releases prior to today's release, dependencies in implementers' projects can still point to the version in [Artifactory](https://regnosys.jfrog.io/ui/repos/tree/General/libs-snapshot/com/isda/cdm) as they do today.

#### _CDM as Java Examples_

Examples of how you can use the CDM can be found in GitHub: [CDM GitHub Examples](https://github.com/finos/common-domain-model/tree/master/examples).

#### _CDM as PYTHON_

CDM as PYHON is available as a set of `.tar.gz` files published [here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-python/).

#### _CDM as DAML_

CDM as DAML is available as a set of `.dar` files published [here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-daml/).

#### _CDM as Scala_

CDM as Scala is available as a `.jar` file published [here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-scala/).

#### _CDM as C# 8.0_

CDM as C# 8.0 is available as a `tar.gz` file containing compiled `.dll` files published [here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-csharp8/).

#### _CDM as C# 9.0_

CDM as C# 9.0 is available as a `tar.gz` file containing compiled `.dll` files published [here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-csharp9/).

#### _CDM as Go_

CDM as Go is available as a `tar.gz` file containing the `.go` files published [here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-golang/).

#### _CDM as TypeScript_

CDM as TypeScript is available as a `tar.gz` file containing the `.ts` files published [here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-typescript/).

#### _CDM as Kotlin_

CDM as Kotlin is available as a `.jar` file published [here](https://central.sonatype.com/artifact/org.finos.cdm/cdm-kotlin/).
