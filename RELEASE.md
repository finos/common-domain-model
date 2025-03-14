# *CDM  5.x.x - Product Qualification Functions for Equity Products*

_Background_

Currently, the product qualification functions in CDM 5.x.x do not fully support qualification of Equity Products. 

_What is being released?_

This release addresses this issue to accommodate Equity and Exotic Products under individual Asset Classes. 

The following qualification functions are being introduced:
  - `Qualify_Equity_OtherSwap` - qualifies a product as an Equity Swap (Other) where the base product qualifies as Equity Swap with non standard terms.
  - `Qualify_Credit_OptionOther` - qualifies a product as a Credit Option (Other) where the base product qualifies as Credit Option with non standard terms.
  - `Qualify_Commodity_OptionOther` - qualifies a product as an Commodity Option (Other) where the base product qualifies as Commodity Option with non standard terms.
  
The following qualification functions are being amended to avoid duplicate qualification:
  - `Qualify_AssetClass_InterestRate` 
  - `Qualify_AssetClass_Equity`

Modifications in the existing functions include the existing clause where only `forwardPayout` is allowed. The functions will also check for a combination `forwardPayout` with `interestRatePayout` or `cashflows` to qualify the product as `AssetClass: Interest Rate` or  `AssetClass: Equity`.

_Backward-incompatible changes_

None.

_Review Directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above, navigating to file cdm > product > qualification > func and reviewing:
- the addition of three new qualification functions
- modifications to existing qualification functions

Original Issue: [#3476](https://github.com/finos/common-domain-model/issues/3476)

Changes can be reviewed in PR: [#3522](https://github.com/finos/common-domain-model/pull/3522)
