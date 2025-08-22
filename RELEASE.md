# *Legal Agreement Model - Additional clauses for Legacy CSA*

_Background_

D2 Legal Technology and ISDA are updating legacy clause definitions within the model related to collateral, notices, and specified conditions under the ISDA Credit Support Annex (CSA) framework.

These clauses are foundational to collateral mechanics and counterparty obligations under the CSA, and are being modernised to align with prevailing legal interpretations and documentation standards. These were the last few clauses which have now been contributed to complete the legacy legal agreement piece.

_What is being released?_

Three new clauses:

- Specified Condition
- Independent Amount
- Other Eligible Support

These new clauses require the creation of the following enums:

- `CSASpecifiedConditionEnum`
- `AdditionalTerminationEventEnum`
- `RatedPartyEnum`
- `IndependentAmountCompareEnum` extends `CreditNotationMismatchResolutionEnum` and adds a new value 'compare'

Updating clauses that exist within the model:

- `CollateralTransferAgreementElections`
  - `finalReturns` added as an attribute
- `HoldingAndUsingPostedCollateral`
  - `additionalLanguage` added as an attribute
- `CreditSupportObligations`
  - `legacyIndependentAmount` added as an attribute
- `eligibleCreditSupport` added as a type and attribute

Further updates to descriptions and addition of docReferences are also being contributed.

_Review Directions_

Changes can be reviewed in PR: [#3979](https://github.com/finos/common-domain-model/pull/3979)

# *Legal Documentation - New types & consolidation of enums for Legacy CSA*

_Background_

D2 Legal Technology and ISDA are updating legacy clause definitions within the model related to collateral, notices, and specified conditions under the ISDA Credit Support Annex (CSA) framework.

These clauses are foundational to collateral mechanics and counterparty obligations under the CSA, and are being modernised to align with prevailing legal interpretations and documentation standards. These were the last few clauses which have now been contributed to complete the legacy legal agreement piece.

_What is being released?_
- Added `CounterpartyRoleEnum` to `PartyContactInformation`. 
- Consolidated `ThresholdRatedPartyEnum` and `MTARatedPartyEnum` into `RatedPartyEnum` and `ThresholdZeroEventEnum` and `MTAZeroEventEnum` into `ZeroEventEnum`
- Created `DemandsAndNotices` type 
- Relabelled `otherEligibleSupport` to `otherEligibleSupportIM` in `CreditSupportObligations` createIQ synonym mapping 
- Changed type of `demandsAndNotices` attribute from `ContactElection`  to `DemandsAndNotices` type in `CreditSupportAgreementElections` & `CollateralTransferAgreementElections`
 
_Review Directions_

Changes can be reviewed in PR: [#3976](https://github.com/finos/common-domain-model/pull/3976)
