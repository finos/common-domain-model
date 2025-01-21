# *CDM Model - CollateralCriteria SpecificAsset and Index fields*

_Background_

`CollateralCriteria` was created as part of [Release 6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90). It is a choice data type, combining all the criteria terms that previously appeared in `AssetCriteria` and `IssuerCriteria`.


A DSL bug blocked the addition of choice data types to `CollateralCriteria`. The bug has since been resolved under DSL 9.27.0 with further details in the DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9

Following the bug fix, `Asset` and `Index` choice data types were added to `CollateralCriteria`. 

_What is being released?_

This release added the fields `Asset` and `Index` to the `CollateralCriteria` data type following the DSL bug fix.

`Asset` was later replaced by a new data type `SpecificAsset` to differentiate it from the already existing `AssetType` attribute.

_Backward-incompatible changes_

None.

_Review Directions_

The addition of the new attributes `Asset` and `Index` to `CollateralCriteria` can be reviewed in PR: [#3321](https://github.com/finos/common-domain-model/pull/3321).
The adition of the attribute `SpecificAsset` to replace `Asset` can be reviewed in PR: [#3335]([url](https://github.com/finos/common-domain-model/pull/3335))
