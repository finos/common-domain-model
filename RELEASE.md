# *Model Optimisation: Extract Party References from Product*

_What is being released_

This change is part of the ongoing model refactoring to externalise the definition of the parties involved in a transaction from the definition of the transacted product.

- New enum `RelatedPartyEnum` specifies all the possible parties roles (other than counterparty) which are defined within the product.​  
- The `RelatedPartyEnum` can be resolved to an actual party by looking up the enum value in type `RelatedPartyReference` to find the corresponding party reference.
- New type `CounterpartyOrRelatedParty` is used when the party can be either one of the counterparties or a related party.​
- `RelatedPartyEnum` is constrained within each usage in the model using conditions.

This release covers the party references in `AdditionalDisruptionEvents.determiningParty`, `DividendReturnTerms.extraordinaryDividendsParty`, `OptionPhysicalSettlement.predeterminedClearingOrganizationParty` and `ExerciseNotice.exerciseNoticeReceiver`.  Future releases will cover the remaining party references defined within the product.

_Review Directions_

In the CDM Portal, use the Textual Browser to review the enum and types mentioned above.

In the CDM Portal, use the Ingestion page to review the following samples:

AdditionalDisruptionEvents.determiningParty:
- `equity > eqs-ex01-single-underlyer-execution-long-form.xml`
- `equity > eqs-ex01-single-underlyer-execution-long-form-related-party.xml`
- `equity > eqs-ex06-single-index-long-form.xml`

DividendReturnTerms.extraordinaryDividendsParty:
- `equity > eqs-ex01-single-underlyer-execution-long-form.xml`
- `equity > eqs-ex01-single-underlyer-execution-long-form-related-party.xml`

OptionPhysicalSettlement.predeterminedClearingOrganizationParty:
- `events > exercise-swaption-physical-related-party.xml`

ExerciseNotice.exerciseNoticeReceiver:
- `rates > ird-ex12-euro-swaption-straddle-cash-related-party.xml`
