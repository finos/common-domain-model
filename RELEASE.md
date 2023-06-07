# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the following dependencies:

`rosetta-dsl` - `7.8.0`: Removes logging implementations from non test scoped classpath

`rosetta-bundle` - `6.6.0`: Windows fix for scheme import test

This release contains no changes to the model or test expectations.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
