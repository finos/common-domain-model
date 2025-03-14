# *CDM Model - Change to CDM 5.x.x for Equity Products*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes

_What is being released?_

This release creates following modifications:
- following new qualification functions are added
  - `Qualify_Equity_OtherSwap` - This function qualifies a product as an Equity Swap (Other) where the base product qualifies as Equity Swap with non standard terms.
  - `Qualify_Credit_OptionOther` - This function qualifies a product as a Credit Option (Other) where the base product qualifies as Credit Option with non standard terms.
  - `Qualify_Commodity_OptionOther` - This function qualifies a product as an Commodity Option (Other) where the base product qualifies as Commodity Option with non standard terms.
  
- changes in existing Qualification functions to avoid duplicate qualification

  - `Qualify_AssetClass_InterestRate` 
  - `Qualify_AssetClass_Equity`

      modifications in the above functions include the modification of existing clause where only `forwardPayout` is allowed. Now, the functions also check for a combination `forwardPayout` with `interestRatePayout` or `cashflows` to qualify the product as `AssetClass: Interest Rate` or  `AssetClass: Equity`

Original Issue: [#3476](https://github.com/finos/common-domain-model/issues/3476)
Changes can be reviewed in PR: [#3522](https://github.com/finos/common-domain-model/pull/3522)
