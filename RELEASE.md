# _Product Qualification - Amendment in filter conditions for Exotic Products_

_Background_

An issue was identified in the product qualification framework following updates introduced in [Issue #3476](https://github.com/finos/common-domain-model/issues/3476), whereby it was possible for products with non-standardised terms to be double-qualified. 

_What is being released?_

This release addresses this issue, amending the following functions that qualify non-exotic products to add a negative `nonStandardisedTerms` check:

* Qualify_CreditDefaultSwaption
* Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName
* Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName
* Qualify_EquitySwap_PriceReturnBasicPerformance_Index
* Qualify_EquitySwap_TotalReturnBasicPerformance_Index
* Qualify_EquitySwap_PriceReturnBasicPerformance_Basket
* Qualify_EquitySwap_TotalReturnBasicPerformance_Basket
* Qualify_EquitySwap_ParameterReturnVariance_SingleName
* Qualify_EquitySwap_ParameterReturnVariance_Index
* Qualify_EquitySwap_ParameterReturnVariance_Basket
* Qualify_EquitySwap_ParameterReturnDispersion
* Qualify_EquitySwap_ParameterReturnVolatility_SingleName
* Qualify_EquitySwap_ParameterReturnVolatility_Index
* Qualify_EquitySwap_ParameterReturnVolatility_Basket
* Qualify_EquitySwap_ParameterReturnCorrelation_Basket
* Qualify_EquitySwap_ParameterReturnDividend_SingleName
* Qualify_EquitySwap_ParameterReturnDividend_Index
* Qualify_EquitySwap_ParameterReturnDividend_Basket
* Qualify_EquityForward_PriceReturnBasicPerformance_SingleName
* Qualify_EquityForward_PriceReturnBasicPerformance_SingleIndex
* Qualify_EquityForward_PriceReturnBasicPerformance_Basket

_Review Directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above, navigating to file cdm > product > qualification > func.

Details of the Issue and the resolution are available here: [#3545](https://github.com/finos/common-domain-model/issues/3545)

Changes can be reviewed in PR: [#3540](https://github.com/finos/common-domain-model/pull/3540)
