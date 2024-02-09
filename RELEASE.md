# _CDM Model - Date Time Functions_

_What is being released?_

This release fixes the existing `ToDateTime` function, and adds `ToTime` function.

- `ToDateTime` - converts a `date` to a `zonedDateTime`, defaulting the `time` to "00:00:00" and the `timezone` to "Z" (UTC)
- `ToTime` - created a `time` from inputs of `hours`, `minutes` and `seconds`

_Review directions_

In Rosetta, select the `Common Domain Model` project, then select the Textual Browser and inspect the functions `ToDateTime` and `ToTime`.

The changes can be reviewed in PR: [#2693](https://github.com/finos/common-domain-model/pull/2693)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-dsl` and `rosetta-bundle` dependencies.

Version updates include:
- `rosetta-dsl` 9.5.0: Adds support for tabulating projection data. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.5.0.
- `rosetta-dsl` 9.6.0: DSL build and compile performance improvements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.6.0.
- `rosetta-bundle` 10.12.0: Adds JSON schema code generator.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR: [#2690](https://github.com/finos/common-domain-model/pull/2690)
