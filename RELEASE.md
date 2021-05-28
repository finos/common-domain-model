# *Product Model - Support for Increase and Decrease as part of a single Trade*

_What is being released?_

The structural definition of a `Tradableproduct` has been adjusted with a new `TradeLot` data type. This type is introduced to represent the trading of multiple trade lots as part of a single trade. This new feature is particularly needed to support use cases like trade increase (upsize) as well as decrease (unwind).
A `Tradableproduct` can have multiple `TradeLot`. They each contain:

- multiple instances of `PriceQuantity`,  
- an `effectiveDate` attribute (optional) to capture the date at which the price and quantity elements become effective in the case of an already open trade
- a `lotIdentifier` as unique identifier

Several functions have been updated to reflect the new definition of `Tradableproduct`. For example, the functions that calculate the performance and cash settlement on the return leg of an equity Swap, have been updated to work in the case of a single trade lot only.
Future work will ensure that those functions also work with multiple trade lots. This requires iteration logic which is not yet supported in the Rosetta DSL today.

All the synonym mappings have been adjusted. All the FpML product sample messages have been interpreted with a single trade lot. 

The relevant sections in the CDM documentation have been updated.

_Review directions_

In the CDM Portal, select the textual browser and review the following data types:

- `TradableProduct`, 
- `TradeLot` and 
- `PriceQuantity`

In the CDM Portal, select the ingestion panel and review any of the FpML product samples.

In the CDM Documentation, review the following sections:

- [Tradable Product](https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#tradableproduct)

# *Event Model - Function Development - Support for Increase and Decrease Events*

_What is being released?_

A new `IncreaseInstruction` type has been added to cater for the Increase business event use case (also known as upsize) where a new trade lot is added to the trade.

A new `DecreaseInstruction` type has been added to cater for the Decrease business event use case (also known as unwind) where the trade lots inscribed on a trade will be decreased.

The `Instruction` type has been augemented with these 2 new types.

The relevant sections in the CDM documentation have been updated.

_Review directions_

In the CDM Portal, select the textual browser and review the following data types:

- `IncreaseInstruction` and
- `DecreaseInstruction`

In the CDM Documentation, review the following sections:

- [Proposed Instruction](https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#proposed-instruction)
