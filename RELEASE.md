# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 8.8.1: Changes to support serialisation to XML. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.8.1.

There are no changes to the model. Test expectations have not changed functionaly, but
the order of the JSON attributes now follow the order of the Rosetta model. Additionally,
enum values in expectations now respect the naming convention of the Rosetta model.

The changes can be reviewed in PR [#2430](https://github.com/finos/common-domain-model/pull/2430).
