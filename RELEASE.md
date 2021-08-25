# *Product Model – Added Support for Commodity Options & Refactor of AveragingObservation*

_What is being released?_

`OptionPayout` has been updated to support vanilla Commodity Options (Bullet European & Asian). The release refactors the `AveragingObservation` object to `AveragingCalculation` and introduces a new property, `ObservationTerms`, to the `OptionPayout` object. The solution decouples the aspects of observing & averaging a price that were previously both contained within `AveragingObservation` and also offers support for cross product observations that are possible for Option contracts. Relevant synonym mappings have been updated so that linkage exists to existing fPML structures.

_Details_

- `AveragingCalculation` - New data type created to replace `AveragingObservation` & defines calculation parameters associated with Average/Asian Options. Modelled in `OptionFeature` with data attribute `averagingRateFeature`.
- `ObservationTerms` - New data type created and included in `OptionPayout` to specify terms associated with observing a benchmark price. Contains a number of existing data types to capture; observable, pricingTime, pricingTimeType, primarySource, secondarySource, precision, observationDate & calculationPeriodDates. 
- `AveragingStrikeFeature` - Object created to keep previous support for average strikes & replaces previous use of `AveragingObservation` in `OptionStrike`. 
- `ParametricDates` - Added to `ObservationDates` to allow specification of date terms in parametric means typically associated with commodities. 

_New Ingestion Examples_

New FpML examples have been added to the CDM distribution & are available in the ingestion pack to validate the changes being made for the representation of Commodity Options and also Commodity Swaps, that was included in a previous release. The examples are:

- `com-ex2-gas-swap-prices-first-day`
- `com-ex3-gas-swap-prices-last-three-days`
- `com-ex4-electricity-swap-hourly-off-peak`
- `com-ex6-gas-call-option`
- `com-ex7-gas-put-option`
- `com-ex9-oil-put-option-american`
- `com-ex27-wti-put-option-asian-listedoption-date`
- `com-ex28-gas-swap-daily-delivery-prices-option-last`
- `com-ex39-basket-option-confirmation`
- `com-ex41-oil-asian-barrier-option-strip`
- `com-ex46-simple-financial-put-option`

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above.

Select the Ingestion view and review the samples in `fpml-5-10 > products > commodity`.
