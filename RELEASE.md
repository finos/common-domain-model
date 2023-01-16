# *Product Model - FpML Mappings - Equities*

_Background_

This release enhances and coverage some of the `Equity` samples mappings that are currently in the model. It also changes some elements in the CDM structure.

_What is being released?_

This release updates and extends the FpML mapping coverage for `Equity products' and modelling changes.
It contains changes in `Index, Security, Asian and AutomaticExercise` types as well as its synonym mappings.


_Types_

base-staticdata-asset-common-type

- Added `exchangeId` into Index type
- Added `futureId` into Index type
- Added `relatedExchangeId` into Index type

- Added `exchangeId` into Security type
- Added `relatedExchangeId` into Security type
- Added `optionsExchangeId` into Security type
- Added `specifiedExchangeId` into Security type


product-common-schedule-type

- Changed date type from `AdjustableOrAdjustedDate` to `AdjustableDates`

product-template-type

- Added `averagingInOut` into Asian type
- Added `marketDisruption` into Asian type
- Added `schedule` into Asian type

- Added `isApplicable`into AutomaticExercise
- Changed cardinality for `followUpConfirmation` in type `ExerciseProcedure` and `thresholdRate` in type `AutomaticExercise`

_Synonyms_
-  Support improved for equity option mappings from FpML element `equityOptionTransactionSupplement`
-  Mapping support for new attribute types described in Types section
-  Added conditional mapping in `priceExpression` that sets to PriceExpressionEnum->AbsoluteTerms when "initialPrice->netPrice->priceExpression" exists and set to PriceExpressionEnum->PercentageOfNotional when "initialPrice->netPrice->priceExpression" exists

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples/packs:

- fpml-5-10->incomplete-products->equity-options

- fpml-5-10->incomplete-products->equity-swaps

