# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the `rosetta-dsl` dependency:

- Version `7.3.1` - Fix Java code-gen bug related to extracting `date` from `zonedDateTime` record type
- Version `7.3.0` - Add support for external rule reference
- Version `7.2.1` - Code-gen generated Java that does not contain keyword clashes

This release contains no changes to the model or test expectations.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
