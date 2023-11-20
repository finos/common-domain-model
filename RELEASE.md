# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 9.1.0: this release includes a rewrite of the Java expression generator, which fixes a number of static compilation errors as well as readability and performance improvements of the generated code. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.0.

The issue brought to light a bug in the model having to do with the `CashPriceQuantityNoOfUnitsTriangulation` function, which has been resolved.

Test expectations remain the same.

The changes can be reviewed in PR [#2533](https://github.com/finos/common-domain-model/pull/2533).
