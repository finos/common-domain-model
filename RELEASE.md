# *Product Model - New Data Types*

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.  Prior to that, this preparatory PR introduces some new data types in the development environment so that they can be seen by participants.

_What is being released?_

- A new enumerator added to support a new approach to identifiers for assets: `AssetIdTypeEnum`.
- New data types added to start the build out of the concept of "Asset" in the product model:
    - `AssetBase` as the base data type to specify common attributes for all Assets
    - `AssetIdentifier` a new data type to uniquely identify an asset, including using the `AssetIdTypeEnum`
    - `Cash` a new data type to represent an asset that is a monetary holding in a currency
    - `DigitalAsset` a new data type to represent an asset that exist only in digital form, eg Bitcoin or Ethereum
    - `ListedDerivative` a new data type to represent an asset that is a securitised derivative on another asset, such as a exchange traded future
- A new `func` namespace created `cdm.base.staticdata.asset.common`, containing three new functions to aid the use of the new `Cash` asset:  `AssetIdentifierByType`, `GetCashCurrency`, `SetCashCurrency`.

None of the existing functionality or modelling is impacted as these changes are standalone at this time.  The proposed `Asset` data type will be defined and introduced later.

_Review Directions_

In Rosetta, open the contribution and view the changes listed above and inspect each of them.

Changes can be reviewed in PR [#2936](https://github.com/finos/common-domain-model/pull/2944)
