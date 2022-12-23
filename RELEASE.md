# *Equity Mappigs - Equity CDM structure updates*


_What is being released?_

- Improvements in equity mappings rate that includes:

	-  support improved for equity option mappings (equityOptionTransactionSupplement)
	-  added isApplicable element in AutomaticExercise CDM type when a FpML sample has a bool indicating if it has an automaticExercise or not.
	-  added exchangeId, futureId and relatedExchangeId in Index CDM type for adding support of mappings
	-  added exchangeId, optionsExchangeId, relatedExchangeId and specifiedExchangeId in Security CDM type for adding support of mappings
	-  added averagingInOut, marketDisruption and schedule in AveragingCalculation for some mappings comming from a feature asian where it was needed.
	-  ObservationSchedule date changed from AdjustableOrAdjustedDate type to AdjustableDates
	-  conditional mapping for priceExpression when equals to AbsoluteTerms inside netPrice
	-  changed cardinality for followUpConfirmation in ExerciseProcedure and tresholdRate in AutomaticExercise for validation fixes.

