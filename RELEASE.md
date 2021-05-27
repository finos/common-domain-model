# *Event Model - Support for Increase and Decrease as part of a single Trade*

_What is being released?_

To be able to support trade increase (upsize) as well as decrease (unwind), a new `TradeLot` data type is introduced that can capture trading in multiple trade lots as part of a single trade.

This `TradeLot` data type contains `PriceQuantity` with multiple cardinality, and in turn replaces the previous `priceQuantity` attribute in `TradableProduct`.

An additional `effectiveDate` attribute (optional) is added to `PriceQuantity`, to capture the date at which such price and quantity become effective in the case of an already open trade.

The ingestion of existing FpML sample documents has been preserved, where each FpML message is interpreted as a single trade lot and mapped accordingly into a trade with a single `tradeLot` attribute.

Functions, for instance to calculate the performance and cash settlement on the return leg of an equity Swap, have been updated to work in the case of a single trade lot only. Developing those functions to work with multiple trade lots will require using iteration logic, which is not yet supported in the Rosetta DSL.

New `IncreaseInstruction` and `DecreaseInstruction` data types that use `TradeLot` are introduced to support the execution of the corresponding increase and decrease business events.

_Review directions_

In the CDM Portal, select the Textual Browser and review the following data types:

- `TradableProduct`, `TradeLot` and `PriceQuantity`
- `IncreaseInstruction` and `DecreaseInstruction`

In the CDM Documentation, review the following sections:

- [Tradable Product](https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#tradableproduct)
- [Proposed Instruction](https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#proposed-instruction)
