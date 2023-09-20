# *Product Model - Qualification - Bond Forwards*

_Background_

This release expands the coverage of the composable product qualification to include bond forwards.

_What is being released?_

The following function updates have been made in the `cdm.product.qualification` namespace:

- Updated `Qualify_AssetClass_InterestRate` to include Bond Forward use case
- Added `Qualify_InterestRate_Forward_Debt` to qualify when a forward payout with a debt security underlier exists, as per the ISDA Taxonomy 
- Updated `Qualify_AssetClass_Equity` to include Bond Forward use case
- Added `Qualify_BaseProduct_EquityForward` to qualify a product as having the ISDA Taxonomy Asset Class classification Equity and Base Product Classification Forward


_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Inspect Pull Request: [#2392](https://github.com/finos/common-domain-model/pull/2392)
