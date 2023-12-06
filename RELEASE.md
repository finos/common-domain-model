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
