# *Legal Documentation - Initial Margin and Variation Margin Clause Updates*

_Background_

D2LT and ISDA are working to enhance the legal documentation aspect of CDM. D2LT has reviewed the Initial Margin / Variation Margin (IM/VM) and Legacy Credit Support documentation and is updating the model to accurately represent the clauses. This includes reducing duplication in the model where possible.

_What is being released?_

Dispute Resolution:
- Merged `valueTerms` and `legacyValue`.
- Merged `resolutionTime` and `legacyResolutionTime`.
- Renamed `LegacyResolutionValue` to `ResolutionValue`.

IneligibleCreditSupport:
- Changed `specifiedParty` type from `Party` to `CounterpartyRoleEnum`.
- Updated type and attribute descriptions.

SensitivityMethodologies:
- Added `partyElection` attribute with a type `SensitivityMethodologiesPartyElection`.
- Split `SensitivityToEquity` into `sensitivityToIndices`, `sensitivityToFunds`, and `sensitivityToETFs`.
- Updated type and attribute descriptions.

Substitution:
- Added `partyElection` attribute with a type `SubstitutionPartyElection`

Covered Transactions:
- Merged `exposure` and `legacyExposure`.
- Updated type and attribute descriptions.

_Review Directions_

Changes can be reviewed in PR: [#4081](https://github.com/finos/common-domain-model/pull/4081)
