# *Model Change - Upgrade Reset and Transfer Function Model to support Interest Rate Payout*

_What is being released?_

This release introduces upgrades to the Reset and Transfer business event functions by adding support for the `InterestRatePayout` data type. 

Expanding on existing functionality which supports `EquityPayout`, this upgrade to support `InterestRatePayout` follows the existing Reset and Transfer mechanism. Namely, the Reset business event function receives as an input the relevant market data `Observation` values and the relevant `Trade` object. The `Transfer` business event function receives the resulting `Reset` business event values as an input to calculate the quantity of the transfer via a performance calculation, which for an `InterestRatePayout` would use the existing `FixedAmount` and `FloatingAmount` calculation functions.

Further, functions relating to the Transfer business event were renamed and refactored to harmonise cash and security transfers. The below functions were change:

* `func Create_CashTransfer` renamed to `Create_Transfer`, which is now the business event function to associate both cash and security transfers to Trades.
* `func Create_CashTransferPrimitive` renamed to `Create_TransferPrimitive`, which now supports creation of transfers of cash and securities.
* `func Create_Transfer` renamed to `Create_CashTransfer`, which creates instances of the `Transfer` data type for cash transfers. The corresponding function to create instances of `Transfer` for security transfers is `Create_SecurityTransfer`. These two functions are referenced in `Create_TransferPrimitive` to associate the newly created `Transfer` to `TradeState`s.


_Review directions_

In the CDM Portal, use the Textual Browser to inspect the changes to the function model. Note that no visual examples have yet been created for the Interest Rate Reset and Transfer events.
