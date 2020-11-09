# *Model Optimisation: Legal Agreement Party References*

_What is being released_

This change is part of the ongoing model refactoring to externalise the definition of the parties involved in a transaction from the definition of the transacted product, and the legal agreements that support the transaction.

This particular release updates the legal model to specify the elective parties using the `CounterpartyEnum` values `Party1` and `Party2`, replacing the existing approach which uses the text "partyA" and "partyB".

- Update all party election types under namespace `cdm.legalagreement.csa`, such as `AccessConditionsElections`, `AdditionalRepresentationElection`, `CalculationAgentTerms` etc, to specify the elective party as type `CounterpartyEnum`. 
- Add `AgreementTerms.counterparty` attribute to specify the `Party` reference and corresponding `CounterpartyEnum`.
- Update all `isda-create` synonyms to use `CounterpartyEnum`.

_Review Directions_

- In the CDM Portal, use the Textual Browser to review the types mentioned above.

- In the CDM Portal, use the Ingestion page to review any `isda-create` samples.
