# *Model Optimisation: Legal Agreement Party References*

_What is being released_

This release updates the legal model to specify the elective parties using the `CounterpartyEnum` values `Party1` and `Party2`, replacing the existing approach which used text values "partyA" and "partyB".

- Update all party election types under namespace `cdm.legalagreement.csa`, such as `AccessConditionsElections`, `AdditionalRepresentationElection`, `CalculationAgentTerms` etc, to specify the elective party as type `CounterpartyEnum`. 
- Add `AgreementTerms.counterparty` attribute to specify the `Party` reference and corresponding `CounterpartyEnum`.
- Update all `isda-create` synonyms to use `CounterpartyEnum`.

_Review Directions_

- In the CDM Portal, use the Textual Browser to review the types mentioned above.

- In the CDM Portal, use the Ingestion page to review any `isda-create` samples.
