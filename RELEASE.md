# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the `rosetta-dsl` dependency:

- Version `7.4.0`:
    - Generate rosetta models from a xsd
    - Bug fix related to code-generated Java that avoids keyword clashes

This release contains no changes to the model or test expectations.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
