# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the rosetta-dsl dependency.

Version updates include:
- 8.7.0: Fixes bug that occurs when `extract` returns `empty`. For further details see DSL issue: https://github.com/REGnosys/rosetta-dsl/issues/655.
- 8.8.0: Syntax update that allows `func` to call a `reporting rule`. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.8.0.

There are no changes to the model, so the test expectations remain the same.

The changes can be reviewed in PR [#2394](https://github.com/finos/common-domain-model/pull/2394).
