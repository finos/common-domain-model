# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the `rosetta-dsl` dependency:

- Versions:
  - `7.10.0` Java code-generation update to allow the default condition implementation to be overridden (via Google Guice) by implementors, similar to how function implementations can be overridden. See also https://github.com/REGnosys/rosetta-dsl/issues/587 for more details.

This release contains no changes to the model or test expectations.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
