# *Model Optimisation: Extract Party References from Product*

_What is being released_

This change is the part of the ongoing model refactoring to externalise the definition of the parties involved in a transaction from the definition of the transacted product.

- New enum `RelatedPartyEnum` specifies all the possible parties roles (other than counterparty) which are defined within the product.​  
- The `RelatedPartyEnum` can be resolved to an actual party by looking up the enum value in type `RelatedPartyReference` to find the corresponding party reference.
- New type `CounterpartyOrRelatedParty` is used when the party can be either one of the counterparties or a related party.​
- `RelatedPartyEnum` is constrained within each usage in the model using conditions.

This release covers the party references in `CalculationAgent.calculationAgentParty`, `Cashflow.payerReceiver.payerRelatedParty` and `Cashflow.payerReceiver.receiverRelatedParty`, and completes the externalisation of parties from the translated product with a few exceptions.

- `PhysicalExercise` and `CashExercise` - the physical and cash exercise models require refactoring to use `TradableProduct`. Once completed, the deprecated attributes `PayerReceiver.payerPartyReference` and `PayerReceiver.receiverPartyReference` can be removed.
- 3rd party payments should be modelled with `SettlementTerms` rather than `Cashflow` - Once completed, the deprecated attributes `PayerReceiver.payerRelatedParty` and `PayerReceiver.receiverRelatedParty` can be removed.

Functions have also been updated to work with counterparties and related parties.

- `Create_Execution`, `Create_ExecutionPrimitive`, `Create_ClearedTrade`, `Create_CashTransferPrimitive`, `CashflowSettlementTerms`, `ExtractCounterpartyBySide`, and `ExtractRelatedParty`.

_Review Directions_

In the CDM Portal, use the Textual Browser to review the types, enums and functions mentioned above.

In the CDM Portal, use the Ingestion page to review the following samples:

CalculationAgent.calculationAgentParty:
- `equity > eqs-ex01-single-underlyer-execution-long-form.xml`
- `equity > eqs-ex01-single-underlyer-execution-long-form-related-party.xml`

Cashflow.payerReceiver.payerRelatedParty and receiverRelatedParty:
- `rates > swap-with-other-party-payment.xml`

PhysicalExercise:
- `events > exercise-swaption-physical.xml`

CashExercise:
- `events > exercise-swaption-cash.xml`
