# _Event Model - Valuation Update_

_Background_

This release introduces new types and functions to support the native representation and qualification of a valuation update. The valuation update can add to or replace the existing valuation history.

_What is being released?_

The following features have been added: 

- Representation of the primitive instruction for a valuation event.
- Application of the valuation primitive instruction to change the trade state.
- Qualification of a valuation update event.

_Data types and attributes_

- Added new `ValuationInstruction` type with the following attributes:
  - `valuation` attribute of type `Valuation`
  - `replace` attribute of type `boolean`
- `valuation` attribute of type `ValuationInstruction` added to `PrimitiveInstruction`.

_Functions_

- Added new `Create_Valuation` function.
- Updated `Create_TradeState` function to include a valuation update step using `Create_Valuation`.

_Qualification_

- Added new `Qualify_ValuationUpdate` function.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

PR: [#2575](https://github.com/finos/common-domain-model/pull/2575)
