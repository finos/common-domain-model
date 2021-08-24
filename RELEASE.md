# *Product Model – FpML Synonym Mappings for Equity Swaps and Equity Options*

_What is being released?_

This release contains a number of FpML synonym mapping fixes for Equity Swaps and Equity Options as detailed below

_Details_

- For Equity Options the `OptionPayout` has a `PayoutQuantity` populated referencing the `PriceQuantity`.
- For all products the `ExternalProductType` is enumerated based on the metadata scheme associated with the product type value.
- For specification of Equity Dividend treatment the following attributes and enumerations, and associated synonym mappings, have been added to data type `DividendReturnTerms`.
  - Attributes `nonCashDividendTreatment, dividendComposition and specialDividends`.
  - Enumerations `DividendCompositionEnum and NonCashDividendTreatmentEnum` for specification of Equity Dividend Treatment.
- Added boolean election `mutualEarlyTermination` to `OptionalEarlyTermination`.
- Added mappings for `multipleExercise` and `partialExercise`.

_Review Directions_

In the CDM Portal, select the Ingestion view and review the samples in `fpml-5-10->products->equity`.
