# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the rosetta-dsl dependency.

Version updates include:
- 8.6.0: Adds annotations to the generated Java code that capture information to better serialise from and to the CDM. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.6.0.
- 8.6.1: Fixes parsing bugs, one related to the `only exists` operation, one responsible for making validation of Rosetta files order dependent. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.6.1.
- 8.6.2: Adds the display name to the annotations of enum values in the generated Java code. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.6.2.

There are no changes to the model, so the test expectations remain the same.

The changes can be reviewed in PR [#2394](https://github.com/finos/common-domain-model/pull/2394).
