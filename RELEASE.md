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

# _Product Model - Qualification - Bond Forwards_

_Background_

This release fixes issue [#2601](https://github.com/finos/common-domain-model/issues/2601) which breaks bond forward qualification.

_What is being released?_

* Function `cdm.product.qualification.Qualify_AssetClass_InterestRate` has been updated to work for forward payouts
* Bond forward FpML samples, and corresponding FpML synonym mappings, have been added

_Review directions_

In Rosetta, open the Common Domain Model workspace, select the Translate tab and review the following samples:

* fpml-5-10 > products > rates > bond-fwd-generic-ex01.xml
* fpml-5-10 > products > rates > bond-fwd-generic-ex02.xml

Changes can be reviewed in PR [#2602](https://github.com/finos/common-domain-model/pull/2602)
