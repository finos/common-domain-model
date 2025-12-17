# *Product Model - Qualification Functions Updates*

_Background_

Qualification functions are used extensively in the Common Domain Model and Digital Regulatory Reporting to determine the type of product.

Several functions were added and modified in the Common Domain Model version 5 without the changes being applied to Common Domain Model versions 6 & 7. This release adds qualification functions to Common Domain Model 6 & 7.

_What is being released?_

Added qualification functions

- `Qualify_Equity_Other_NonStandard` - This function qualifies a product as an Equity Swap (Non-Standard) where the base product qualifies as Equity Swap with non-standard terms.
- `Qualify_Credit_Option_NonStandard` - This function qualifies a product as a Credit Option (Non-Standard) where the base product qualifies as Credit Option with non-standard terms.
- `Qualify_Commodity_Option_NonStandard` - This function qualifies a product as an Commodity Option (Non-Standard) where the base product qualifies as Commodity Option with non-standard terms.

Modified qualification functions

- `Qualify_AssetClass_InterestRate`
- `Qualify_AssetClass_Equity`

_Review Directions_

Changes can be reviewed in PR: [#4252](https://github.com/finos/common-domain-model/pull/4252)
