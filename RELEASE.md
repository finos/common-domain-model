# *CDM Model - Change to CDM 5.x.x for Equity Products*

_Background_

This release contains modifications required to accommodate Equity and Exotic Products under individual Asset Classes. 

_What is being released?_

This release creates following modifications:
- following new qualification functions are added
  - `Qualify_TotalReturnSwap_Index` - This function qualifies a product as a Total Return Swap (Index) where the base product qualifies as Credit Swap and index underlier for performance leg qualifies as Credit.
  - `Qualify_Equity_OtherForward` - This function qualifies a product as a Equity Forward (Other) where the base product qualifies as Equity Forward with non standard terms.

- changes in existing Qualification functions to avoid duplicate qualification

  - `Qualify_AssetClass_Credit` - this function is modified to allow for the qualification of Total Return Swap (Index) with index as underlier
  - `Qualify_UnderlierProduct_Equity` - this function is modified to correctly evaluate the products with index underlier as Equity

- relaxation in the cardinality of `componentId` in `listId` from `2` to `1`

The carnality of `componentId` is relaxed since a package can only have one component at the inception of the trade, which can grow into multiple components as the trade progresses. 

_Review Directions_

In Rosetta, 
1. Select the Textual Browser and inspect the changes identified in qualification functions at:
cdm > product > qualification > func:

`Qualify_TotalReturnSwap_Index`
`Qualify_Equity_OtherForward`
`Qualify_AssetClass_Credit`
`Qualify_UnderlierProduct_Equity`

2. Select the Textual Browser and inspect the changes identified in componentId  at:
Cdm > base > staticdata > identifier > type > IdentifiedList

Original Issue: [#3544](https://github.com/finos/common-domain-model/issues/3544)
Changes can be reviewed in PR: [#3612](https://github.com/finos/common-domain-model/pull/3612)
