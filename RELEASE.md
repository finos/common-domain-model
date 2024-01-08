# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-bundle` dependency.

Version updates include:
- `10.0.0` : Ingestion performance improvements related to the loading of xml schema files

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR https://github.com/finos/common-domain-model/pull/2609

# _Product model - Natural Person and NaturalPersonRole circular reference_

_Background_

An issue regarding a circular reference inside the `NaturalPerson` type was recently found in the model.
`NaturalPerson` and `NaturalPersonRole` are located at the same level inside `Party`, to follow the same structure that `Party` and `PartyRole` have inside the `Trade` type. The circular reference issue appears because the `NaturalPerson` type also contains a `NaturalPersonRole`, which references back to the containing type of `NaturalPerson`, causing a circular reference in the model.

_What is being released?_

This release deprecates the use of the attribute `personRole` of type `NaturalPersonRole`, inside the `NaturalPerson` type, to avoid this circular reference. This attribute is to be fully removed in a subsequent major version, but is only annotated as deprecated in this major version because it is a backward-incompatible change.

- Added the `[deprecated]` annotation to the `personRole` attribute of type `NaturalPersonRole`, from `NaturalPerson`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2576

# _Product Model - ISO Country Code Enum Update_

_Background_

Rosetta has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

This release updates `ISOCurrencyCodeEnum` to keep it in sync with the latest ISO 4217 coding scheme.

* Removed enum value `SLL <"Leone">`

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

Changes can be reviewed in PR [#2595](https://github.com/finos/common-domain-model/pull/2605)

