# _Python Generator v2_

_What is being released?_
This release uses the new version of the Python generator (v2) which includes the following changes:

- Migration to Pydantic 2.x
- More comprehensive support for Rosetta's operators
- Resolves the defect exposed by [PR 2766](https://github.com/finos/common-domain-model/pull/2766)
- Includes an update to the Python Rosetta runtime library used to encapsulate the Pydantic support (now version 2.0.0)
  
_Review directions_

The changes can be reviewed in PR: [#2869](https://github.com/finos/common-domain-model/pull/2869)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-bundle` and `rosetta-dsl` dependencies.

Version updates include:
- `rosetta-bundle` 10.16.0: FpML Coding schema updated.
- `rosetta-dsl` 9.8.0: this release features three new operations - `to-date`, `to-date-time` and `to-zoned-date-time` - to convert a string into a `date`, `dateTime` or `zonedDateTime` respectively. It also adds support to convert these three types into a string using the `to-string` operation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.8.0.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR: [#2877](https://github.com/finos/common-domain-model/pull/2877)
