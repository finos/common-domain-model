# *Event Model - Representation of trade valuations*

_What is being released?_

This release introduces the `Valuation` data type that will document the valuation details of a trade during its life cycle. The history of all the valuations will be inscribed in the new `ValuationHistory` attribute of a `TradeState`.  Future work will explore how to codify the state transitions for valuations.

_Review Directions_

In the CDM Portal, select the textual view or graphical view and inspect:

  - the structural definition of the `Valuation` data type and associated enum type `ValuationTypeEnum`, `ValuationSourceEnum`
  - the insertion of the valuationHistory attribute for the `TradeState` data type


