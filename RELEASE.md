# *Product/Event Model: Averaging Model*

_What is being released_

This change introduces a multi-purpose model for calculating an average of market observations according to contract terms for options. The model can be used to specify the parametric input needed for making an average calculation to determine a strike price or a settlement amount. Specific changes are noted below:

- New type `AveragingObservation`, which defines parameters for use in cases when a valuation or other term is based on an average of market observations.
- New type `ObservationDates`, which describes date details for a set of observation dates in parametric or non-parametric form.
- New type `FxRateObservable`, which defines foreign exchange (FX) asset class specific parameters for market observations.
- New type `ObservationSchedule`, which specifies a single date on which market observations take place and specifies optional associated weighting.
- New enum `AveragingCalculationMethodEnum`, which specifies enumerations for the type of averaging calculation.
- Modified type `OptionFeature` in which the attribute `asian` is replaced with `averagingRateFeature` which is of type `AveragingObservation`.
- Modified type `OptionStrike` in which the attribute `averagingStrikeFeature` is added and is of type `AveragingObservation`. Also modified the choice condition to include `averagingStrikeFeature`.

_Review Directions_

In the CDM Portal, use the Textual Browser to review the types, enums, and functions mentioned above, or use the Graphical Navigator. For example, in the Graphical Navigator, search for `OptionFeature` and then drill down on the `averagingRateFeature` and underlying data types.
