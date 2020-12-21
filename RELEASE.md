# *CDM Model: Primitive Harmonisation Phase 3 - Transfer Primitive*

_What is Being Released_

Primitive harmonisation for Transfer Primitive, which now completes the harmonisation process for all Primitives.

*Background*

Harmonisation will allow for easier combination of Primitives to form complex business events. Changing `before` and `after` attributes to use the same data type eliminates the need for data translations when combining Primitives.

Whilst data translation can be addressed in the Function Model, defining the applicable data types correctly in the Data Model removes the need for additional complexity.

*Model Changes*

The `TransferPrimitive` data type has been updated to make use of `TradeState` data type for both `before` and `after` attributes.

`TradeState` has been updated to additionally include the `transferHistory` attribute, which is represented by the `Transfer` data type.

`Transfer` represents the movement of cash, security or commodities between two parties, which can be a result of negotiated events, for example partial terminations, or scheduled events, for example Interest Rate Swap coupon payments. In either case, the definition of terms relating to settlement can be referenced via the `settlementOrigin` attribute. Representation of the settlement status is done using the workflow model and as such uses `WorkflowStatusEnum` (encapsulated by `BusinessEvent`).

The `Create_CashTransfer` function defines how `Transfer`s should be created in any given cash transfer event. To support the existing Equity Swap coupon payment example, which focuses on cash transfers, functions such as `EquityCashSettlementAmount` were updated and referenced from `Create_CashTransfer`.

Further, descriptions and necessary data types for Allocation, Exercise and Reset business events have been upgraded to reflect the latest model changes.

*Sample File Changes*

A number of sample files relating to Transfers were removed as they are no longer valid in the new model. Applicable scenarios will be updated and reintroduced as part of subsequent releases.

*Review Directions*

In the CDM Portal, use the Textual Browser or Graphical Navigator to inspect changes to the model, referencing the data types and functions mentioned above. Navigate to Functions to inspect the Equity Swap reset example, which has a transfer component.