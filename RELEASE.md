# _Product Qualification Functions - Update to fix the double qualification issue for Equity Products_

_Background_

This pull request refines the CDM's product qualification framework by incorporating a specific identifier for exotic products. Leveraging existing qualification logic, the update introduces the 'nonStandardisedTerms' attribute. A 'True' value for this attribute designates an exotic product, while its absence or a 'False' value signifies a standard product. This ensures consistent and accurate product classification, improving data integrity

_What is being released?_

Modification to following Functions - Addition of `nonStandardisedTerms` check

	•	Qualify_CreditDefaultSwaption
	•	Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName
	•	Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName
	•	Qualify_EquitySwap_PriceReturnBasicPerformance_Index
	•	Qualify_EquitySwap_TotalReturnBasicPerformance_Index
	•	Qualify_EquitySwap_PriceReturnBasicPerformance_Basket
	•	Qualify_EquitySwap_TotalReturnBasicPerformance_Basket
	•	Qualify_EquitySwap_ParameterReturnVariance_SingleName
	•	Qualify_EquitySwap_ParameterReturnVariance_Index
	•	Qualify_EquitySwap_ParameterReturnVariance_Basket
	•	Qualify_EquitySwap_ParameterReturnDispersion
	•	Qualify_EquitySwap_ParameterReturnVolatility_SingleName
	•	Qualify_EquitySwap_ParameterReturnVolatility_Index
	•	Qualify_EquitySwap_ParameterReturnVolatility_Basket
	•	Qualify_EquitySwap_ParameterReturnCorrelation_Basket
	•	Qualify_EquitySwap_ParameterReturnDividend_SingleName
	•	Qualify_EquitySwap_ParameterReturnDividend_Index
	•	Qualify_EquitySwap_ParameterReturnDividend_Basket
	•	Qualify_EquityForward_PriceReturnBasicPerformance_SingleName
	•	Qualify_EquityForward_PriceReturnBasicPerformance_SingleIndex
	•	Qualify_EquityForward_PriceReturnBasicPerformance_Basket

_Review Directions_
In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above, navigating to file cdm > product > qualification > func and reviewing:
	•	modifications to above mentioned qualification functions

Original Bug Issue: [#3545](https://github.com/finos/common-domain-model/issues/3545)

Changes can be reviewed in PR: [#3540](https://github.com/finos/common-domain-model/pull/3540)
