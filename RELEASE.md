# *Product and Event Model - Migrate Java function implementations to native DSL*

_What is being released?_

Following the recent addition of DSL syntax features, functions that were previously implemented in Java due to unsupported DSL features, have now been migrated to native DSL.

The migrated function namespace and names are listed below:

cdm.base.datetime
- `GetAllBusinessCenters`
- `BusinessCenterHolidaysMultiple`

cdm.event.common
- `UpdateIndexTransitionPriceAndRateOption`
- `FindMatchingIndexTransitionInstruction`

cdm.observable.asset.fro
- `IndexValueObservationMultiple`

cdm.observable.event
- `ResolveObservationAverage`

cdm.product.asset
- `ResolveEquityInitialPrice`

cdm.product.asset.calculation
- `GetFixedRate`
- `GetNotionalAmount`
- `GetNonNegativeScheduleStepValues`

cdm.product.asset.floatingrate
- `GetRateScheduleAmount`
- `GetRateScheduleStepValues`

_Review Directions_

In the CDM Portal, select Textual Browser and review the functions above.
