# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the `rosetta-dsl` dependency:

- Versions:
    - `7.5.4` Fixed DSL issue related to `typeAlias` - see https://github.com/REGnosys/rosetta-dsl/issues/554 

This release contains no changes to the model or test expectations.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
