# *Product Qualification functions for Equity Products*

_Background_

This release contains modifications related to: 
- qualification of Equity and Credit Exotic Products under individual Asset Classes. 

The following qualification functions have been added: 

	1. `Qualify_TotalReturnSwap_Index` - This function qualifies a product as a Total Return Swap (Index) where the base product qualifies as Credit Swap and index underlier for performance leg qualifies as Credit.
 
	2. `Qualify_Equity_OtherForward` - This function qualifies a product as a Equity Forward (Other) where the base product qualifies as Equity Forward with non-standard terms.

- The following functions have been modified:
  
  - `Qualify_AssetClass_Credit` - This function is modified to allow for the qualification of Total Return Swap (Index) with index as underlier
  
  - `Qualify_UnderlierProduct_Equity` - This function is modified to evaluate the products with index underlier as Equity

- Cardinality requirement relaxed for `componentId` - The cardinality of `componentId` is relaxed from a minimum requirement of `2 components ids` to a `single component id`, since a package can have only one component at the inception of the trade. The components of the package can grow into multiple components as the trade progresses. 

_What is being released?_

- Added qualification functions
  - `Qualify_TotalReturnSwap_Index`
  - `Qualify_Equity_OtherForward`

- Modified functions

  - `Qualify_AssetClass_Credit`
  - `Qualify_UnderlierProduct_Equity`

- Relaxation in the cardinality of:
  - `componentId` in `listId`

_Review Directions_

In the Rosetta platform, 
1. Select the Textual Browser and inspect the changes identified in qualification functions at:
   
CDM > product > qualification > func:
  - `Qualify_TotalReturnSwap_Index`
  - `Qualify_Equity_OtherForward`
  - `Qualify_AssetClass_Credit`
  - `Qualify_UnderlierProduct_Equity`

2. Select the Textual Browser and inspect the changes identified in componentId  at:
   
CDM > base > staticdata > identifier > type > IdentifiedList

Original Issue: [#3544](https://github.com/finos/common-domain-model/issues/3544)

Changes can be reviewed in PR: [#3612](https://github.com/finos/common-domain-model/pull/3612)
