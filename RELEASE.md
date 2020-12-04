# *Model Optimisation: Extract Party References from Product*

_What is being released_

This change is the part of the ongoing model refactoring to externalise the definition of the parties involved in a transaction from the definition of the transacted product.

Updates the model naming based on feedback from the Architecture Review Committee:

- Rename `RelatedPartyEnum` to `AncillaryRoleEnum`.
- Rename `CounterpartyEnum` to `CounterpartyRoleEnum`.
- Rename `RelatedPartyReference.relatedParty` to `AncillaryParty.role`.
- Rename `Counterparty.counterparty` to `Counterparty.role`.
- Rename `TradableProduct` attributes with multiple cardinality to be singular, e.g. `TradableProduct.counterparty` and `TradableProduct.ancillaryParty`.

Updates the model to specify the ancillary role consistently regardless of whether the party is a counterparty or not:

- Remove type `CounterpartyOrRelatedParty`.
- Update attributes `CalculationAgent.calculationAgentParty`, `AdditionalDisruptionEvents.determiningParty`, `DividendReturnTerms.extraordinaryDividendsParty`, `OptionPhysicalSettlement.predeterminedClearingOrganisationParty` and `ExerciseNotice.exerciseNoticeReceiver` to use enum `AncillaryRoleEnum`.
- Add conditions to `TradableProduct` type to ensure that if an `AncillaryRoleEnum` is specified in the product, there is a corresponding `AncillaryParty` entry. 

_Review Directions_

In the CDM Portal, use the Textual Browser to review the types mentioned above.

In the CDM Portal, use the Ingestion page to compare the samples below, noting that the ancillary roles `CalculationAgentIndependent`, `ExtraordinaryDividendsParty` and `DisruptionEventDeterminingParty` are specified consistently in both samples:

- `equity > eqs-ex01-single-underlyer-execution-long-form.xml`
- `equity > eqs-ex01-single-underlyer-execution-long-form-other-party.xml`
