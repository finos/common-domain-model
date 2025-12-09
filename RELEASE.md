# *Product - Modifying and adding qualification functions*

_Background_

Product - Modifying and adding qualification functions

Qualification functions are used extensively in CDM and Digital Regulatory Reporting to determine the type of product.

Several functions were added and modified in CDM 5 without the changes being applied to CDM 6 & 7. This update adds qualification functions to CDM 6 & 7

_What is being released?_

Added qualification functions

- `Qualify_Equity_OtherSwap` - This function qualifies a product as an Equity Swap (Other) where the base product qualifies as Equity Swap with non standard terms.
- `Qualify_Credit_OptionOther` - This function qualifies a product as a Credit Option (Other) where the base product qualifies as Credit Option with non standard terms.
- `Qualify_Commodity_OptionOther` - This function qualifies a product as an Commodity Option (Other) where the base product qualifies as Commodity Option with non standard terms.

Modified qualification functions

- `Qualify_AssetClass_InterestRate`
- `Qualify_AssetClass_Equity`

_Review Directions_

Changes can be reviewed in PR: [#4252](https://github.com/finos/common-domain-model/pull/4252)
