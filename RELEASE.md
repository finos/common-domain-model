# *Legal Agreement - High-Level CSA and CTA Refactoring Clause Updates*

_Background_

This contribution enhances Legal Agreements in CDM. Members of the Legal Agreement Working Group have approved the changes below as it streamlines this part of the model and reduces validation errors while improving data integrity and enforcement of conditions and cardinality.

_What is being released?_

- `SpecifiedCondition` and `AccessCondition` merged into same structure.
- Updated description for `CalculationAgentTerms`.
- Updated description for `CustodyArrangements` and cardinality for `CustodianEvent`.
- Updated `docReference` and description for `NotificationTime`.
- Updated description for `OtherAgreements`.
- Moved `value` into `OtherEligibleandPostedSupport`.
- Updated cardinality for `TerminationCurrencyElection`.
- Updated cardinalities within `CoveredTransactions`.
- Updated cardinality for `ThresholdElection`.
- Updated cardinality for `MTAElection`.
- Removed `CSADatedasofDate` and `CSAMadeOn` date (as they are already covered) and renamed type to `MasterAgreementDatedAsOfDate`.
- `LegacyDeliveryAmount` and `LegacyReturnAmount` are renamed to `DeliveryAmount` and `ReturnAmount`.
- Updated description for `ValuationAgent`.
- Type name and Enum name updates.

_Review Directions_

Changes can be reviewed in PR: [#4216](https://github.com/finos/common-domain-model/pull/4216)
