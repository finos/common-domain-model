# _Implementation of the Standardized Schedule Method for Initial Margin Calculation in CDM_
# _Standardised Schedule - Key Features_

_Background_

The financial crisis that began in 2007 highlighted significant weaknesses in the resilience of banks and market participants to financial and economic shocks. To address these vulnerabilities and curb excessive risk-taking, the Basel Committee on Banking Supervision (BCBS) published the D499 document in April 2020, detailing new margin requirements aimed at reducing systemic risk and promoting central clearing. These requirements mandate that Initial Margin (IM) be calculated using either a quantitative model or a standardized method, ensuring uniformity and comprehensive coverage of counterparty risk.

_What is being released?_

The implementation of the Standardized Schedule Method for calculating Initial Margin (IM) within the Common Domain Model (CDM) is being released. This release introduces a structured approach to computing IM, providing a conservative and straightforward alternative for market participants who either lack the resources to develop quantitative models or prefer not to use third-party services.

Key components of this release include:
- New enumerations have been created to cover the asset typologies and subtypes of financial products included in this methodology.
- Functions have been developed to extract the duration and notional of financial products in accordance with specification D499.
- After extracting the necessary data, additional functions have been developed to compute the initial margin by integrating the extracted data with the model formulas.
- A package of examples of various covered products has been assembled to test the functionality of the implemented functions.

Data types

- Added new `StandardizedSchedule` type.
- Added new `GrossInitialMarginAndMarkToMarketValue` type.
- Added new `CreditEvent` type.

Enumerations

- Added new `StandardizedScheduleAssetClassEnum` enumeration.
- Added new `StandardizedScheduleProductClassEnum` enumeration.

Functions

- Added new `AdjustableDateResolution` function.
    - A fall back for unadjustedDate when adjustedDate is only available.
- Added new `AdjustableOrAdjustedOrRelativeDateResolution` function.
    - A fall back for unadjustedDate when adjustedDate is only available.
- Added new `AuxiliarEffectiveDate` function.
    - Extracts the effective date of specific products such as interest rate swaps and swaptions.
- Added new `AuxiliarTerminationDate` function.
    - Extracts the termination date of specific products such as interest rate swaps and swaptions.
- Added new `BuildStandardizedSchedule` function.
    - Takes a trade and uses qualification to extract the relevant information to populate the grid that will be used to calculate the gross initial margin.
- Added new `DateDifferenceYears` function.
    - Computes the difference in years between two dates. All years are supposed to have 365 days.
- Added new `EconomicTermsForProduct` function.
    - Extracts the economic terms from a product.
- Added new `FXFarLeg` function.
    - Extracts the far leg of an FX swap (deliverable or not) based on two criteria: the forward payout with the latest value date or the forward payout with the latest settlement date.
- Added new `GetGrossInitialMarginFromStandardizedSchedule` function.
    - Takes the grid information from an specific trade and calculates the gross initial margin.
- Added new `GetIMRequirement` function.
    - Computes the IM requirement, which is required in the calculation of the gross initial margin. It depends exclusively on the asset class of the trade and, in some cases, on the duration as well.
- Added new `GetNetInitialMarginFromBaseCurrencyExposure` function.
    - Computes the net initial margin, taking the gross initial margin result and the mark to market value."
- Added new `IsCreditNthToDefault` function.
    - Identifies a product as a CR basket Nth to default.
- Added new `IsFXDeliverableOption` function.
    - Identifies a product as an FX deliverable option.
- Added new `IsFXNonDeliverableOption` function.
    - Identifies a product as an FX non-deliverable option.
- Added new `IsIRSwaptionStraddle` function.
    - Identifies a product as an IR swaption straddle.
- Added new `IsIRSwapWithCallableBermudanRightToEnterExitSwaps` function.
    - Identifies a product as an IR swap with bermudan/callable right to enter/exit swaps.
- Added new `ProductForTrade` function.
    - Extracts the product from a trade.
- Added new `StandardizedScheduleAssetClass` function.
    - Identifies the asset class of a trade, according to the standardized schedule classification.
- Added new `StandardizedScheduleCommodityForwardNotionalAmount` function.
    - Extracts the notional amount of a CO forward. Floating price forwards not supported.
- Added new `StandardizedScheduleCommoditySwapFixedFloatNotionalAmount` function.
    - Extracts the notional amount of a CO fixed float swap.
- Added new `StandardizedScheduleDuration` function.
    - Extracts the duration of a trade, according to the product class-depending extraction method defined in the ISDA industry survey.
- Added new `StandardizedScheduleEquityForwardNotionalAmount` function.
    - Extracts the notional amount of an EQ forward.
- Added new `StandardizedScheduleFXNDFNotional` function.
    - Extracts the notional amount and currency of an FX non-deliverable forward.
- Added new `StandardizedScheduleFXNDONotional` function.
    - Extracts the notional amount and currency of an FX non-deliverable option.
- Added new `StandardizedScheduleFXSwapNotional` function.
    - Extracts the notional amount and currency of an FX swap.
- Added new `StandardizedScheduleFXVarianceNotionalAmount` function.
    - Extracts the notional amount of an FX variance swap.
- Added new `StandardizedScheduleMonetaryNotionalCurrencyFromResolvablePQ` function.
    - Extracts the notional currency for all products that have it populated in the resolvable priceQuantity.
- Added new `StandardizedScheduleMonetaryNotionalFromResolvablePQ` function.
    - Extracts the notional amount for all products that have it populated in the resolvable priceQuantity.
- Added new `StandardizedScheduleNotional` function.
    - Extracts the notional amount of a trade, according to the product class-depending extraction method defined in the ISDA industry survey.
- Added new `StandardizedScheduleNotionalCurrency` function.
    - Extracts the notional currency of a trade, according to the product class-depending extraction method defined in the ISDA industry survey.
- Added new `StandardizedScheduleOptionNotionalAmount` function.
    - Extracts the notional amount of a CO or EQ option.
- Added new `StandardizedScheduleProductClass` function.
    - Identifies the product class of a trade, according to the standardized schedule classification.
- Added new `StandardizedScheduleVarianceSwapNotionalAmount` function.
    - Extracts the notional amount of an EQ variance swap.
- Added new `UnderlierForProduct` function.
    - Extracts the underlier product.

_Review directions_

In the Rosetta Platform, select the Textual Browser and inspect each of the changes identified above.

In the Rosetta Platform, select Function and select the ones you want to test and upload a sample from our test pack.

The changes can be reviewed in PR: [#3140](https://github.com/finos/common-domain-model/pull/3140)


# _Infrastructure - Dependency and Reference Data Update_

_What is being released?_

This release updates the `DSL` dependency. 

Version updates include:
- `DSL` 9.16.3: bug fix for tabulators and validation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.3
- `DSL` 9.16.5: performance improvements for tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.5
- `DSL` 9.16.6: bug fix for meta generation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.6
- `DSL` 9.16.7: bug fix for report generation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.7
- `DSL` 9.17.0: new syntax features. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.17.0
- `DSL` 9.17.1: bug fix for global key generation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.17.1
- `DSL` 9.17.2: bug fix for ingestion id mapping. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.17.2

This release also updates `ISOCurrencyCodeEnum` based on updated reference data scheme ISO Standard 4217.

Version updates include:
- removed value: `ZWL <"Zimbabwe Dollar">`

_Review directions_ 

The changes can be reviewed in PR: [#3125](https://github.com/finos/common-domain-model/pull/3125)
