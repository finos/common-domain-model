# *Event Model - Transfer: Added Quantity and Observable attributes*

_What is being released?_

This release adjusts the `Transfer` type by replacing the `PriceQuantity` attribute, with separate `Quantity` and optional `Observable` attributes.  In addition, a data constraint has been added to the `Transfer` type to validate that when an `Observable` is specified that the `Quantity->unitOfAmount->financialUnit` is also specified.

```
type Transfer:
   identifier Identifier (0..*)
      [metadata scheme]
   quantity Quantity (1..1)
   observable Observable (0..1)
   payerReceiver PartyReferencePayerReceiver (1..1)
   settlementDate AdjustableOrAdjustedOrRelativeDate (1..1)
   settlementOrigin SettlementOrigin (0..1)

   condition:
      if observable exists
      then quantity -> unitOfAmount -> financialUnit exists
```

_Review directions_

In the CDM Portal, select the Ingestion, and review samples in the Events and Bundles folders. 
