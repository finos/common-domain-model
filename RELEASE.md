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
- Added `partyElection` attribute with a type `SubstitutionPartyElection`.

Covered Transactions:
- Merged `exposure` and `legacyExposure`.
- Updated type and attribute descriptions.

_Review Directions_

Changes can be reviewed in PR: [#4081](https://github.com/finos/common-domain-model/pull/4081)

## Legal documentation - Updating the Legacy Threshold and Minimum Transfer Amount types

*Background*

D2LT and ISDA are working to enhance the legal documentation aspect of CDM. D2LT has reviewed the Initial Margin / Variation Margin (IM/VM) and Legacy Credit Support documentation and is updating the model to accurately represent the clauses. This includes reducing duplication in the model where possible.

*What is being released?*

The following updates were applied to Threshold and Minimum Transfer Amount (MTA) types:

Threshold
- Merged Legacy threshold and threshold types.
- Added party elections to specify the party to which the threshold applies.
- Changed infinity attribute to boolean.
- Added conditions for robustness.
- Change fixed amount type to money.
- Added Zero event to fixed amount.
- Updated descriptions of types and attributes.

Minimum Transfer Amount
- Merged Legacy MTA and MTA.
- Added party elections to specify the party to which the MTA applies.
- Added conditions for robustness.
- Change fixed amount type to money.
- Added Zero event to fixed amount.
- Updated descriptions of types and attributes.

*Review Directions*

Changes can be reviewed in PR: [#4107](https://github.com/finos/common-domain-model/pull/4107)
