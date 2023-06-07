# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the following dependencies:

* `rosetta-dsl` - `7.8.0`: Logging implementations removed from classpath to allow Java users of the CDM to select their own logging implementation

* `rosetta-bundle` - `6.6.0`: Includes a fix for the model enum generator (a scheme import test) specific for Microsoft Windows

This release contains no changes to the model or test expectations.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
