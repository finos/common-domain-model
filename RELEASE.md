# _Product Model - Asset Refactoring in AssetCriteria_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets.  A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes some additional functionality (following three planned major tranches of work in CDM 6 to implement the refactored model).

_What is being released?_

AssetCriteria:
- The attribute `assetIdentifier` has been refactored to model an actual asset, specified using the `Asset` choice data type, rather than just an identifier. The attribute name has also been updated to `specificAssets` to make it clear that it is a list of specific assets, all of whom are eligible to be pledged as collateral.  The condition on the data type has been updated too.
  
ListingType:
- The cardinality of the three attributes in the data type `ListingType` (which is used in `AssetCriteria`) has been changed to none-to-many (rather than none or one); the attributes are `exchange`, `sector`, `index`. Without this, it would be only possible to select one of the values.

_Review directions_

The changes can be reviewed in PR: [#3228](https://github.com/finos/common-domain-model/pull/3228)

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- All references to the attribute `assetIdentifier` on `AssetCriteria` need to be updated as referenced; the new attribute is `asset`.
