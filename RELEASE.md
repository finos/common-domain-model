# *CDM Model - Addition of SpecificAsset to CollateralCriteria*

_Background_

The choice data type `CollateralCriteria` was introduced in [Release 6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90). It combines all the criteria terms that previously appeared in `AssetCriteria` and `IssuerCriteria`.

The `Asset` choice data type was originally included in `CollateralCriteria` but was deemed difficult to differentiate from the `AssetType` attribute. This release is an enhancement from `Asset` to the new data type `SpecificAsset` to improve the usability of the model.

_What is being released?_

This release added `SpecificAsset` to the `CollateralCriteria` attributes, to replace the original `Asset`.

_Backward-incompatible changes_

None.

_Review Directions_

The addition of the new attributes `Asset` to `CollateralCriteria` can be reviewed in PR: [#3321](https://github.com/finos/common-domain-model/pull/3321)

The adition of the attribute `SpecificAsset` to replace `Asset` can be reviewed in PR: [#3335](https://github.com/finos/common-domain-model/pull/3335)
