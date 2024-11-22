# _Product Model_ - Asset Refactoring of FloatingRateIndex and InterestRateIndex

_Background_

The Asset Refactoring initiative (see [#2805](https://github.com/finos/common-domain-model/issue/2805)) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes a minor adjustment following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

The names of the two terms `FloatingRateIndex` and `InterestRateIndex` have been flipped to make the latter, `InterestRateIndex` to be the higher level term such that an interest rate index can be either a floating rate index or an inflation rate index.

Rationale:
- Consistent with the name `InterestRatePayout`, which operates on both floating rates and inflation rates.
- Consistent with the name `FloatingRateIndexEnum`.
- Consistent with how "floating rate option" or "FRO" is understood in other places in the model.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- Rename the data type `FloatingRateIndex` to be called `InterestRateIndex`. 
- Update `InterestRateIndex` to be a choice data type with two attributes: `FloatingRateIndex` and `InflationIndex`.
- Update the attribute `rateOption` on the data type `FloatingRateBase` to be of type `InterestRateIndex` as the base class is used for both floating and inflation indices.
- In addition, the name swap has been implemented in the following types:
  - `PriceQuantity`
  - `IndexTransitionInstruction`
- and the following functions:
  - `FindMatchingIndexTransitionInstruction`
  - `Qualify_IndexTransition`
  - `UpdateIndexTransitionPriceAndRateOption`
  - `InterestRateObservableCondition`
  - `EvaluateCalculatedRate`
  - `IndexValueObservation`
  - `IndexValueObservationMultiple`
  - `GetFloatingRateProcessingType`
  - `Qualify_Transaction_OIS`
- and the following mappings:
  - `cdm.mapping.fpml.confirmation.tradestate:synonym`
  - `cdm.mapping.ore:synonym`
- The following two functions have been moved from the `cdm.observable.asset.fro` namespace to the `cdm.observable.asset.func` namespace as they no longer act on a `fro` ie floating rate index:
  - `IndexValueObservation`
  - `IndexValueObservationMultiple`.

_Review directions_

The changes can be reviewed in PR: #3267 or in Rosetta.
