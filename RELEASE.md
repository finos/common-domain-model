# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-dsl` and `rosetta-bundle` dependencies.

Version updates include:

- `rosetta-dsl` 9.4.0: this release improves performance of validating Rosetta code and of generating code. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.4.0.
- `rosetta-bundle` 10.9.3: this release adds mapping support for XSD substitution groups, which fixes the issue related to the mapping of FpML oilPhysicalLeg xml elements.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR https://github.com/finos/common-domain-model/pull/2672.
