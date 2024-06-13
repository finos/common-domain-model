# _Product Model - Fix for Portfolio Return Terms_

_Background_

The purpose of this update is to modify an existing condition relating to `Basket` which is causing unexpected behavior as well as a change in the cardinality required of `PerformancePayout` and `PortfolioReturnTerms`.

_What is being released?_

- Condition attached to `Basket` has been updated to `required choice basketConstituent, portfolioBasketConstituent` instead of previous condition: `one-of`.
- Cardinality of `PerformancePayout` attributes has been updated to `(0..*)` instead of `(0..1)` :
    - initialValuationPrice,
    - interimValuationPrice
    - finalValuationPrice
- Cardinality of `PortfolioReturnTerms` attributes has been updated to `(0..*)` instead of `(0..1)` :
    - quantity
    - initialValuationPrice
    - interimValuationPrice
    - finalValuationPrice

_Review Directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: [#2978](https://github.com/finos/common-domain-model/pull/2978)

# *Product Model - Quantity Change For Existing Trade Lot*

_Background_

This release adds support for price and/or quantity changes on an existing TradeLot, as described in issue [#2923](https://github.com/finos/common-domain-model/issues/2923).

_What is being released?_

The `Create_QuantityChange` function has been updated to allow the price and/or quantity to be updated on an existing `TradeLot`. If the `QuantityChangeInstruction->lotIdentifier` matches the trade's `TradeLot->lotIdentifier`, then the price and/or quantity with matching units are updated based on the direction (i.e. `Increase`, `Decrease`, `Replace`) specified in the instructions.

The existing functionality is unchanged for an increase, i.e., if the `QuantityChangeInstruction->lotIdentifier` does not match the trade's `TradeLot->lotIdentifier`, then a new `TradeLot` is created.

_Review Directions_

In GitHub, review the following JSON sample files that have been added to represent the updates to an existing `TradeLot`.

- `cdm-sample-files/functions/business-event/quantity-change/increase-equity-swap-existing-trade-lot-func-input.json`
- `cdm-sample-files/functions/business-event/quantity-change/increase-equity-swap-existing-trade-lot-func-output.json`
- `cdm-sample-files/functions/business-event/quantity-change/partial-termination-equity-swap-func-input.json`
- `cdm-sample-files/functions/business-event/quantity-change/partial-termination-equity-swap-func-output.json`

In Rosetta, select the Visualisation tab and review the following examples in the Quantity Change Business Event folder:

- Increase Equity Swap with Existing Trade Lot
- Partial Termination Equity Swap

Changes can be reviewed in PR [#2980](https://github.com/finos/common-domain-model/pull/2980)

# *Product Model - FpML Mapping Update*

_Background_

This release adds FpML mapping fixes and improvements that have been previously implemented in other models such as Digital Regulatory Reporting (DRR).

_What is being released?_

- FpML synonyms to map `EventInstruction` attributes `intent`, `eventDate` and `effectiveDate`
- FpML synonyms to map `EconomicTerms` attribute `nonStandardisedTerms`
- FpML synonyms to map `WorkflowState` attribute `workflowStatus`
- FpML synonyms and mapper to map commodity schedule xml elements `calculationPeriodsSchedule` and `calculationPeriods` into `PriceSchedule->datedValue`

_Review Directions_

In Rosetta, select the Translate tab and review the following samples:

- fpml-5-10 > processes > msg-cleared-alpha-trade-CFTC-SEC-and-canada.xml
- fpml-5-10 > processes > msg-ex52-execution-advice-trade-partial-novation-C02-00.xml
- fpml-5-10 > incomplete-processes > msg-ex60-execution-advice-trade-amendment-correction-F02-10.xml
- fpml-5-13 > products > commodity-derivatives > com-mockup-ex1-strikePricePerUnitSchedule.xml
- fpml-5-13 > products > commodity-derivatives > com-mockup-ex2-strikePricePerUnitSchedule.xml

Changes can be reviewed in PR [#2982](https://github.com/finos/common-domain-model/pull/2982)

# *CDM Distribution - Python Code Generation*

_What is being released?_

This release updates the `bundle` dependency to version `11.10.0` to include the new version of the Python generator which includes the following changes:

- added support for model name clashes with Python keywords, soft keywords, and items whose names begin with "_"
- added support for DSL operators `to-string` and `to-enum`
- resolves the defect exposed by PR [#2766](https://github.com/finos/common-domain-model/pull/2766)
- includes an update to the Python runtime library used to encapsulate the Pydantic support (now version 2.0.0)

_Review directions_

Download the latest Python distribution from the [Maven Central](https://central.sonatype.com/artifact/org.finos.cdm/cdm-python)

The changes can be reviewed in PR: [#2986](https://github.com/finos/common-domain-model/pull/2986)
