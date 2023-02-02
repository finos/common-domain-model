# *Equity mappings - equity Model*

_Background_

This release contains the content discussed on the ARC regarding the `Equity Mappings` work in order to enhance the support and cover more elements of it.  

_What is being released?_

- Mapping coverage enhanced for `equityOptionTransactionSupplement`
- Added mapping and element `isApplicable` (change in the model) in `AutomaticExercise` 
- Changed cardinality of `thresHoldRate` and `followUpConfirmation` from (1..1) to (0..1) in order to reduce Validation issues with condition `ExerciseProcedureChoice`
- Conditional mapping added to support priceExpression inside PriceExpression type. 

_Review Directions_

In Rosetta, select `CDM for Digital Regulatory Reporting` project, then the Translate tab, and review samples in `fpml-5-10 > incomplete-products > equity-options` `fpml-5-10 > incomplete-products > equity-swaps`
