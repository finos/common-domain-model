# *Product Model - Performance Payout - Variance and Dividend Options*

_Background_

The new `Payout` `performancePayout` was recently introduced to allow for representation of a wider variety of products. In this release, support for Options with a performance underlier has been introduced. In these products the use of `PerformancePayout` is not direct, but indirect, as the product underlying the option.

_What is being released?_

- Minor changes to Performance Payout to enable it to accommodate Variance and Dividend Options.
- Mapping coverage for Variance and Dividend Equity Options.
- Qualification functions for Variance, Volatility, Correlation and Dividend Options.

_Types_

product-asset-type

- Removed `extraordinaryEvents` from `VarianceReturnTerms` type.
- Removed `extraordinaryEvents` from `VolatilityReturnTerms` type.
- Added `multipleExchangeIndexAnnexFallback` to `Valuation` type.
- Added `componentSecurityIndexAnnexFallback` to  `Valuation` type.

product-template-type

- Added `EquitySpecificAttributes` condition to `PerformancePayout` type.
- Added `expirationTimeType` attribute to `EuropeanExercise` type.

_Enumerations_

- Added `ExpirationTimeTypeEnum`.

_Translate_

synonym-cdm-fpml

Added mapping coverage for Variance, Volatility, Correlation and Dividend Options:

- Qualify_EquityOption_ParameterReturnVariance_SingleName
- Qualify_EquityOption_ParameterReturnVariance_Index
- Qualify_EquityOption_ParameterReturnVariance_Basket
- Qualify_EquityOption_ParameterReturnVolatility_SingleName
- Qualify_EquityOption_ParameterReturnVolatiliy_Index
- Qualify_EquityOption_ParameterReturnVolatiliy_Basket
- Qualify_EquityOption_ParameterReturnCorrelation_Basket
- Qualify_EquityOption_ParameterReturnDividend_SingleName
- Qualify_EquityOption_ParameterReturnDividend_Index
- Qualify_EquityOption_ParameterReturnDividend_Basket

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

fpml-5-10/products/variance-swaps

- eqvs-ex06-variance-option-transaction-supplement
- eqvs-ex07-variance-option-transaction-supplement-pred-clearing

fpml-5-10/products/dividend-swaps

- div-ex04-dividend-swap-option-transaction-supplement
- div-ex05-dividend-swap-option-gs-example
- div-ex06-dividend-swap-option-pred-clearing

# *Product Model - FpML mappings for Equity*

_What is being released?_

This release fixes FpML product synonym mapping issues for FpML `equityOption` samples, focusing on settlement type and currency.

_Review Directions_

In the CDM Portal, select Ingestion and review the samples specified below.

* fpml-5-10/incomplete-products/equity-options
* fpml-5-10/products/equity