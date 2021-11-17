# *Event Model - Normalisation of the Create Function for Quantity Change Primitive *

_What is being released?_

This release develops a normalised functional expression of the Create_ function for a quantity change primitive event.

_Details_

Following the addition of price settlement cashflows to the PriceQuantity structure, the functional expression of `Create_QuantityChangePrimitive` has been adjusted to account for price settlement cashflow inputs. The function now takes as arguments:

- `PriceQuantity`, which can contain price settlement cashflows, instead of just `Quantity`
- `QuantityChangeDirectionEnum`, which is a new enumeration to specify whether the quantity change is an increase, a decrease or a replacement (the quantity change being always specified as a positive number)
- `Identifier` to (optionally) specify an identifier for the trade lot being increased or decreased

These attributes have been assembled to form a new `QuantityChangeInstruction` data type, which has been added to `Instruction`. That single data type supersedes the previously separate `IncreaseInstruction` and `DecreaseInstruction` data types, where the increase/decrease direction is now handled via an enumeration.

The normalised Create_ function for quantity change primitive event is now being utilised in a number of Create_ event functions:

- `Create_StockSplit`
- `Create_Termination`
- `Create_ClearedTrade`
- `Create_Return`

The function visualisation samples for Partial and Full Termination have been adjusted to reflect the new function argument structure. 

_Review Directions_

In the CDM Portal, select the Textual Browser and review the aforementioned data types and functions. Select the Instance Viewer and review the following samples:

- CREATE TERMINATION BUSINESS EVENT
