# *Product Model - Price and Quantity Schedule*

_Background_

This (non-functional) release begins to refactor the price and quantity components so they can accomodate schedules.

_What is being released?_

The `Price` and `Quantity` data types both extend a basic type called `MeasureBase`, which represents a measure as a combination of a value and a unit.

This release maintains the common ancestry of `Price` and `Quantity` to `MeasureBase` but introduces additional ancestors to allow those components to support schedules.

Specifically, a new `MeasureSchedule` data type now extends `MeasureBase` with an additional schedule of date and value pairs, instead of just a single value. New `PriceSchedule` and `QuantitySchedule` data types are introduced that both extend `MeasureSchedule` with the same system of units and multiplier attributes previously found on `Price` and `Quantity`.

The existing `Price` (resp. `Quantity`) data type is now a specialised extension of `PriceSchedule` (resp `QuantitySchedule`) that requires the additional schedule attribute to be absent. This structure allows to introduce those additional base components while preserving the original behaviour of `Price` and `Quantity` as they are currently used to model products and events.

_Review Directions_
 
In the CDM Portal, select Textual Browser and view the attributes and data types above.
