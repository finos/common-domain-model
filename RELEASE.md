# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-dsl` and `rosetta-bundle` dependencies.

Version updates include:
- `rosetta-dsl` 9.5.0: Adds support for tabulating projection data. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.5.0.
- `rosetta-dsl` 9.6.0: DSL build and compile performance improvements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.6.0.
- `rosetta-bundle` 10.9.3: Adds mapping support for XSD substitution groups, which fixes the issue related to the mapping of FpML oilPhysicalLeg xml elements.
- `rosetta-bundle` 10.12.0: Adds JSON schema code generator.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR: [#2684](https://github.com/finos/common-domain-model/pull/2684) / [#2686](https://github.com/finos/common-domain-model/pull/2686)