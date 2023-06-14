# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the `rosetta-dsl` dependency:

- Versions:
    - `7.9.3` Made keyword `then` mandatory to avoid ambiguity. Improved consistency of using
      square brackets. See also https://github.com/REGnosys/rosetta-dsl/issues/569 for more details and information on usage.

The model has been updated to conform to the new DSL syntax. No logical changes to
the model have been made, thereby the test expectations remain the same.

_Review directions_

CDM Java implementors should update their maven `pom.xml` to the latest CDM maven artefact (groupId com.isda, artifactId cdm) and recompile.
