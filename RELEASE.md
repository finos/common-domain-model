## Legal documentation - Updating and Merging Legacy Threshold and Minimum Transfer Amount types

*Background*

D2LT and ISDA are working to enhance the legal documentation aspect of CDM. D2LT has reviewed the Initial Margin / Variation Margin (IM/VM) and Legacy Credit Support documentation and is updating the model to accurately represent the clauses. This includes reducing duplications in the model where possible.

*What is being released?*

The following updates were applied to Threshold and Minimum Transfer Amount (MTA) types:

Threshold
- Merged Legacy threshold and threshold.
- Added party elections. Changed infinity to boolean.
- Added conditions for robustness.
- Updated fixed amount to the money type.
- Added Zero event to fixed amount.
- Updated descriptions.

Minimum Transfer Amount
- Merged Legacy MTA and MTA.
- Added party elections.
- Added conditions for robustness.
- Updated fixed amount to the money type.
- Added Zero event to fixed amount.
- Updated descriptions.

*Review Directions*

Changes can be reviewed in PR: [#4107](https://github.com/finos/common-domain-model/pull/4107)
