# *Legal Documentation - Consolidation of enums & creation of new types for Legacy CSA*

_Background_

D2 Legal Technology and ISDA are updating legacy clause definitions within the model related to collateral, notices, and specified conditions under the ISDA Credit Support Annex (CSA) framework.

These clauses are foundational to collateral mechanics and counterparty obligations under the CSA, and are being modernised to align with prevailing legal interpretations and documentation standards. These were the last few clauses which have now been contributed to complete the legacy legal agreement piece.

_What is being released?_
- Added `CounterpartyRoleEnum` to `PartyContactInformation`. 
- Consolidated `ThresholdRatedPartyEnum` and `MTARatedPartyEnum` into `RatedPartyEnum` and `ThresholdZeroEventEnum` and `MTAZeroEventEnum` into `ZeroEventEnum`
- Created `DemandsAndNotices` type 
- Relabelled `otherEligibleSupport` to `otherEligibleSupportIM` in `CreditSupportObligations` createIQ synonym mapping 
- Changed type of `demandsAndNotices` attribute from `ContactElection`  to `DemandsAndNotices` type in `CreditSupportAgreementElections` & `CollateralTransferAgreementElections`
 
- _Review Directions_

Changes can be reviewed in PR: [#3976](https://github.com/finos/common-domain-model/pull/3976)
