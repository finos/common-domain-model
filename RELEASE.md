# *CDM Model: PriceQuantity refactoring follow up tasks - minor changes for Reset, OptionStrike, and FloatingRate*

_What is Being Released_

The recent `PriceQuantity` refactoring introduced a new standard set of data types for `Price` and `Quantity` and also propagated the related changes throughout most of the model.  This release continues that propagation in specific areas and includes other minor clean up tasks:

1. Updated `Reset`->`resetValue` and `Observation`->`observationValue`type to use the `Price` data type. 

2.  In `OptionStrike`
  - Removed metadata address from `StrikePrice`:  This attribute uses the new standard `Price` data type, but does not require the new metadata address pointing to `PriceQuantity` because the strike price is not represented there.  This change corrects an unintended change from the `PriceQuantity` refactoring release.
  - Changed the condition `choice` to `one of`, which is a more streamlined expression that can be used in this case.

3. Tangentially related to `PriceQuantity`: Expanded the description for `BusinessEvent`->`EventDate`.

_Review directions_

In the CDM Portal, select the Textual Browser and search for the data types and attributes listed above.
