# *Infrastructure - CDM Artifacts published to Maven Central*

#### _What is being released?_

As part of the CDM migration to FINOS, all generated artifacts are being published to Maven Central.

#### _Download Source Code_

The CDM source code was previously available to download from the CDM Portal. After this release, the open source code is only available to be downloaded from [CDM GitHub](https://github.com/finos/common-domain-model). All current and previous released versions are tagged and can be downloaded from [CDM GitHub Releases](https://github.com/finos/common-domain-model/releases).

#### _CDM as Java_

In order to use the CDM in a [Maven](https://maven.apache.org) project, the following dependency needs to be added to the project pom.xml:

```
<dependency>
  <groupId>org.finos.cdm</groupId>
  <artifactId>cdm-java</artifactId>
  <version>LATEST</version>
</dependency>
```

The "LATEST" value can be substituted for any [CDM GitHub Releases](https://github.com/finos/common-domain-model/releases) from this release. For previous releases, use the existing mechanism.

#### _CDM as Java Examples_

Examples of how you can use the CDM can be found in GitHub: [CDM GitHub Examples](https://github.com/finos/common-domain-model/tree/master/examples).

#### _CDM as DAML_

CDM as DAML is available as a set of `.dar` files published [here](https://oss.sonatype.org/content/repositories/snapshots/org/finos/cdm/cdm-daml/).

#### _CDM as Scala_

CDM as Scala is available as a `.jar` file published [here](https://oss.sonatype.org/content/repositories/snapshots/org/finos/cdm/cdm-scala/).

#### _CDM as C# 8.0_

CDM as C# 8.0 is available as a `tar.gz` file containing compiled `.dll` files published [here](https://oss.sonatype.org/content/repositories/snapshots/org/finos/cdm/cdm-csharp8/).

#### _CDM as C# 9.0_

CDM as C# 9.0 is available as a `tar.gz` file containing compiled `.dll` files published [here](https://oss.sonatype.org/content/repositories/snapshots/org/finos/cdm/cdm-csharp9/).

#### _CDM as Go_

CDM as Go is available as a `tar.gz` file containing the `.go` files published [here](https://oss.sonatype.org/content/repositories/snapshots/org/finos/cdm/cdm-golang/).

#### _CDM as TypeScript_

CDM as TypeScript is available as a `tar.gz` file containing the `.ts` files published [here](https://oss.sonatype.org/content/repositories/snapshots/org/finos/cdm/cdm-typescript/).

#### _CDM as Kotlin_

CDM as Kotlin is available as a `.jar` file published [here](https://oss.sonatype.org/content/repositories/snapshots/org/finos/cdm/cdm-kotlin/).
