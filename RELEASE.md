# *Model Optimisation: Initial fees and additional payments*

_What is being released_

The `Create_Execution` function has been modified to include an input for the initial settlement information for the transaction such as a fee or upfront payment. 

The existing `SettlementTerms` type has been used for this and has been adjusted to contain all relevant information: date, parties, and payer/receiver, plus an assetIdentifier to uniquely link this cashflow settlement to a cashPrice specification.

_Review Direction_

In the Textual Broswer of the CDM Portal:

-  Type `Execution` has been modified to have a one-to-many relationship with `SettlementTerms` to support multiple initial settlement terms.
- `PayerReceiver` has been added to `SettlementTerms` to define the settlement parties 
- `AssetIdentifier` has been added to `SettlementTerms`  to uniquely link the cashflow settlement to a cashPrice specification
- Updated Create_Execution to take `SettlementTerms` as an input
- Updated descriptions to align with the change in the data structure:
  - Search for type `SettlementTerms` - see updated description
  - Search for type `Execution`, scroll down to attribute `settlementTerms` - see updated description

In the Instance Viewer of the CDM Portal, see the `exection business event` examples:

- `Swaption` - Premium is represented as a settlement term
- `Swap With Other Party Payment` - Broker fee is represented as a settlement term
- `Swap With Initial Fee` - Fee is represented as a settlement term
