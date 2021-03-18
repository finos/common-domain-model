# *CDM Model: PriceQuantity refactoring follow up tasks - minor changes for Reset, OptionStrike, and Floating Rate*

_What is Being Released_

The recent `PriceQuantity` refactoring introduced a new standard set of data types for `Price` and `Quantity` and also propogated the related changes throughout most of the model.  This release continues that propogation in specific areas and includes other minor clean up tasks:

1. Updated type to `Price` for `Reset`->`resetValue` and `Observation`->`observationValue`. 

2.  In `OptionStrike`
  - Removed metadata address from `StrikePrice`:  This attribute uses the new standard `Price` data type, but does not require the new metadata address pointing to `PriceQuantity` because the strike price is not represented there.  This change corrects an unintended change from the `PriceQuantity` refactoring release.
  - Changed the condition `choice` to `one of`, which is a more streamlined expresssion that can be used in this case.

3. Removed all inline comments introduced in the `PriceQuantity` refactoring release:

  - `FloatingRate`->`observable` was commented out and now has been removed entirely.
  - `FloatingRateSpecification`->`initialRate`:  The [metadata address "pointsTo"=PriceQuantity->price] was commented out and now has been removed entirely.
  - `QuantityMultiplier`->`multiplierValue`: The  [metadata address "pointsTo"=PriceQuantity->quantity] was commented out and now has been removed entirely.

4. Tangentially related to `PriceQuantity`: Expanded the descprition for `BusinessEvent`->`EventDate`.

_Review directions_

In the CDM Portal, select the Textual Browser and search for the data types and attributes listed above.
