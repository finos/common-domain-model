# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 9.1.2: this release fixes DSL issues [#670](https://github.com/REGnosys/rosetta-dsl/issues/670) and [#653](https://github.com/REGnosys/rosetta-dsl/issues/653). For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.2.
- `rosetta-dsl` 9.1.3: this release fixes an issue related to the generated Java `process` method. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.3.

_Review directions_

There are no functional changes to the model. In the expectation files, global keys and references have been updated due
to a bug fix, but they remain semantically the same.

The changes can be reviewed in PR [#2550](https://github.com/finos/common-domain-model/pull/2550).
