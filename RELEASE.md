# *CDM - Eligible Collateral enhancements for Securities Lending*

_Background_

This release contains enhancements to the Eligible Collateral representation based
upon the work recently undertaken by ISLA to support collateral schedules
used for securities lending in the CDM.

_What is being released?_

There are 3 areas being enhanced:

1. Added a new `ConvertiblePreference` item to the `EquityTypeEnum`
2. Added a new `OutstandingBalance` item to the `ConcentrationLimitTypeEnum`
3. Added a new type `AssetLocation` which forms part of the `CollateralCriteria` Choice

_Backward-incompatible changes_

The addition of `AssetLocation` is a backward incompatible change. 

_Review Directions_

The change can be reviewed in PR: [#3439](https://github.com/finos/common-domain-model/pull/3439).
