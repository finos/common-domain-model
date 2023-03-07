# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the CDM library dependencies including: 

- Dependency `rosetta-dsl` updated to version `7.1.0` - contains bug fixes, a simplified dependency structure, and security updates
- Dependency `ingest-test-framework` updated to version `5.5.0` - contains bug fixes related to synonym conditional mapping

The release contains no changes to the model or test expectations.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
