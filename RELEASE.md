# _Product Model - ISO Country Code Enum Update_

_Background_

Rosetta has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

This release updates `ISOCurrencyCodeEnum` to keep it in sync with the latest ISO 4217 coding scheme.

* Removed enum value `SLL <"Leone">`

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

Changes can be reviewed in PR [#2594](https://github.com/finos/common-domain-model/pull/2604)

# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 9.2.0: this release moves deployment of DSL artifacts to Maven Central. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.2.0.
- `rosetta-dsl` 9.3.0: this release contains syntax highlighting improvements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.3.0.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR [#2607](https://github.com/finos/common-domain-model/pull/2607).
