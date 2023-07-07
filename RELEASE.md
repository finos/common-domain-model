# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the rosetta-dsl dependency:

- Version upgrade includes:
  - 8.2.0: new operations have been added to convert strings into enum values (`to-enum`), numbers (`to-number`/`to-int`), and time values (`to-time`). Additionally, converting enum values or values of a basic type into a string can be performed via the new operation `to-string`. No changes are required in the CDM. See release notes for examples and details: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.2.0

_Review Directions_

CDM Java implementors should update their maven pom.xml to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
