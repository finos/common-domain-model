# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the `rosetta-dsl` dependency:

- Versions:
    - `7.5.0` Support for ISO data types - see DSL documentation https://github.com/REGnosys/rosetta-dsl/issues/512 
    - `7.5.2` Import model from xsd updated to support different documentation tags
    - `7.5.3` Additional changes make ISO data types backwards compatible

This release contains no changes to the model or test expectations.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
