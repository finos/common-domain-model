# *Product Qualification functions for Equity Products*

_Background_

In order to enhance the capability to model equity products, CDM Product Qualification model requires additional functions for `Total Return Swap (Index)` and `Equity Forward (Other)`. Additionally, existing functions also require modifications to qualify Equity Products in the required asset classes. This release contains these modifications required to accommodate Equity and Exotic Products. The details of the required modication is avaialble in [Issue #3544](https://github.com/finos/common-domain-model/issues/3544)

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
