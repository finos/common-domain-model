# _Observable Asset Model - Add new operand types_

_Background_

Additional operand types are needed for Spread, BorrowFee, MarkUpDown, VenueExecutionFee, ClearingFee and Tax. Yield is needed
as a new price type.

_What is being released?_

- Added Spread, BorrowFee, MarkUpDown, VenueExecutionFee, ClearingFee and Tax to `PriceOperandEnum`.
- Added Yield to `PriceTypeEnum`.
- Added `CentralSecuritiesDepository` and `InternationalCentralSecuritiesDepository` to `PartyRoleEnum`.

_Backward-Incompatible Changes_

None

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.
