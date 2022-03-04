# *Event model - Transfers*

_Background_

Transfers components are not represented consistently in the CDM for post-trade events.  Initiation trade event generated transfers are represented in a `PriceQuantity` object attached to a `Trade`, where as post trade event generated transfers are represented as a `Transfer` object embedded in a business event instruction.  This inconsistency creates ambiguity for implementors and creates complexity when defining functional reporting rules.  In addition, for post trade events a Transfer can only be defined in terms that require the CDM functional model to calculate the Transfer amount, implementors cannot provide a pre-determined amount.

The CDM Transfer model is therefore being refactored based on three principles:

- Develop a standardised approach for the representation of a Transfer which is used consistently across all event types.
- Align the representation of transfers with the composable `BusinessEvent` model.
- Rewrite the functional logic of Transfers so that a Business Event instruction is defined in terms of a defined Transfer amount.  The functional model is retained for calculating a Transfer amount but the creation of a Business Event is not dependent on leveraging the functionality.

Following the refactoring any transfers associated with an event would be captured as part of the `Instruction` to create the `BusinessEvent`.  This would be used to create a `TransferHistory` associated with the `TradeState`.  The `PriceQuantity` would therefore contain representation of notional and prices only.

_What is being released?_

This release contains the components required to represent Transfers in line with the above approach.  Visualisation and model to model mappings have been updated to reflect this refactoring.  Changes are described based on the data types, enumerations and functions impacted:

_Data Types_

- `TradeState`- contains an attribute `transferHistory` which has been updated to reference the data type `TransferState`.
- `TransferState` - specifies a Transfer that has been effected by a business or lifecycle event and the state of the `Transfer` through its lifecycle.
- `Transfer` – updated to define a movement of assets between two parties, information about the `settlementOrigin` and `resetOrigin` of the transfer, and a `transferExpression` defining the nature of the transfer amount and its source.
- `TransferStatus` – new data type that defines where a `Transfer` is in its lifecycle.
- `SettlementOrigin` - updated data type containing a reference to the payout that was the origin of the transfer amount, where applicable.
- `TransferExpression` - Defines the nature of the transfer amount in terms of a fee type (e.g. Premium, Termination) or transfer associated with a scheduled or contingent event on a contract (e.g. Exercise, Performance, Credit Event)
- `TransferInstruction` – updated to contain a list of defined `TransferState` to be added to a `TradeState`
- `CalculateTransferInstruction` – contains instructions for calculating a transfer associated with a TradeState with reference to a payout and any necessary resets.
- `Instruction` – attribute `primitiveInstruction` has been updated from multiple to single cardinality so that an instruction is defined in terms of a single primitiveInstruction containing a list of basic primitive instruction attributes.
- `PrimitiveInstruction` – has been updated to remove the `one-of` condition allowing a list of primitive instruction attributes to be defined. `transfer` has been added allowing a `TransferInstruction` to be specified.

_Enumerations_

- `FeeTypeEnum` - updated to be an attribute within `TransferExpression` defining a transfer amount in terms of a fee type.

_Functions_

- `Create_TradeState` - updated function to add `TransferHistory` to a `TradeState` when 
- `Create_TransferPrimitive` – updated to take input of a `TransferInstruction` containing a list of `TransferState`, and creates a `TradeState` with updated `TransferHistory`.
- `CalculateTransfer` – new function that takes input of a `CalculateTransferInstruction`, calculates a Transfer amount and creates output of a `Transfer`.
- `Create_CashTransfer` – takes input of a `CalculateTransferInstruction`, calculates a Transfer amount using function `ResolveTransfer` and creates output of a `Transfer`.
- `ResolveTransfer` – new function that takes input of a `CalculateTransferInstruction` containing a `Payout` and associated `ResetHistory` and creates output of a Transfer.
- `SecurityFinanceCashSettlementAmount` – updated function to create output of a `Transfer`.
- `EquityCashSettlementAmount` – updated function to create output of a `Transfer`.
- `InterestCashSettlementAmount` – updated function to create output of a `Transfer`.

_Visualisations_

The following Visualisations have been updated to contain Transfers represented using the updated model.

- `Quantity Change Business Event > Partial Termination Equity Swap`
- `Quantity Change Business Event > Partial Termination Vanilla Swap`
- `Quantity Change Business Event > Full Termination Equity Swap`
- `Quantity Change Business Event > Full Termination Vanilla Swap`
- `Execution Business Event > Interest Rate Swap with Initial Fee`

_Translate_

Model to model mappings have been updated to reflect the above model refactoring

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the data types, enumerations and functions listed above.
In the CDM Portal, select the Instance Viewer and review the Visualisation samples above.
In the CDM Portal, select Ingestion and review the following example transactions:
- fpml-5-10 > products > equity > eqd-ex01-american-call-stock-long-form
- fpml-5-10 > products > rates > ird-initial-fee
