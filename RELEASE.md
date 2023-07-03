# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the rosetta-dsl dependency:

- Version upgrades included:
  - 8.0.0: this version is a step towards the harmonisation of the expression syntax in rules and functions. No changes are required in the CDM. See release notes at https://github.com/REGnosys/rosetta-dsl/releases/tag/8.0.0
  - 8.1.0: this version contains various patches to improve model validation while editing the model. No changes are required in the CDM. See release notes at https://github.com/REGnosys/rosetta-dsl/releases/tag/8.1.0

_Review Directions_

CDM Java implementors should update their maven pom.xml to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
