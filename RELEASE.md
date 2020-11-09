# *Model Optimisation: Extract Party References from Product*

_What is being released_

This change is the part of the ongoing model refactoring to externalise the definition of the parties involved in a transaction from the definition of the transacted product.

Update `AgreementTerms` model to specify the two parties to the agreement elections using the `CounterpartyEnum` values `Party1` and `Party2`.  The `Party` corresponding to each enum value can be specified using `AgreementTerms.counterparty`.

_Review Directions_

- In the CDM Portal, use the Textual Browser to review the types mentioned above.

- In the CDM Portal, use the Ingestion page to review any `isda-create` samples.
