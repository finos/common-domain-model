# *Product Model - FpML Mappings - Equities*

_What is being released?_

This release updates and extends the FpML mapping coverage for Equity products.

-  Support improved for equity option mappings from FpML element `equityOptionTransactionSupplement`
-  Added `isApplicable` attribute in type `AutomaticExercise` mapped to the FpML element `automaticExercise`
-  Added attributes `exchangeId`, `futureId` and `relatedExchangeId` in type `Index`
-  Added attributes `exchangeId`, `optionsExchangeId`, `relatedExchangeId` and `specifiedExchangeId` in type `Security`
-  Added attribtes `averagingInOut`, `marketDisruption` and `schedule` in type `AveragingCalculation`
-  `ObservationSchedule->date` was moved from type `AdjustableOrAdjustedDate` to `AdjustableDates`
-  Changed cardinality for `followUpConfirmation` in type `ExerciseProcedure` and `thresholdRate` in type `AutomaticExercise`

_Review Directions_

In the CDM Portal, select Ingestion and review examples in the folder `fpml-5-10 > products > equity`
