# _Product Model - Updates to Qualification of AssetClass_

_Background_

Some products are currently not covered by the "ISDA Taxonomy V2 Level 1 - ASSETCLASS" functions.
In particular, a gap in index underliers has been identified and is resolved by this update. For further information please see issue [#2762](https://github.com/finos/common-domain-model/issues/2762).

_What is being released?_

This release modifies functions Qualify_AssetClass_* along the below three axes:

- Added inclusive checks on `underlier -> index -> productTaxonomy -> primaryAssetClass`
- Aligned security criteria to index criteria: switch from checking `security->securityType` to checking `security->productTaxonomy -> primaryAssetClass`
- Added inclusive checks on `forwardUnderlier` under some functions where only the `optionUnderlier` was previously considered

Modified functions:

- `Qualify_AssetClass_InterestRate`
- `Qualify_AssetClass_Credit` 
- `Qualify_AssetClass_ForeignExchange` 
- `Qualify_AssetClass_Equity` 
- `Qualify_AssetClass_Commodity`

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect the changes identified above.

The changes can be reviewed in  PR: [#2841](https://github.com/finos/common-domain-model/pull/2841)
