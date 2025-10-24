# *Legal Documentation - Misc IM/VM Clause Updates*

_Background_

D2LT and ISDA are working to enhance the legal documentation aspect of CDM. D2LT has reviewed the IM/VM and Legacy Credit Support documentation and is updating the model to accurately represent the clauses. This includes the elimination of duplications in the model.

_What is being released?_

1. Dispute Resolution: Merged valueTerms and legacyValue. Merged resolutionTime and legacyResolutionTime. Changed LegacyResolutionValue to ResolutionValue.
2. IneligibleCreditSupport: Changed specifiedParty to CounterpartyRoleEnum.
3. SensitivityMethodologies: Added partyElection and split SensitivityToEquity into sensitivityToIndices, sensitivityToFunds, sensitivityToETFs.
4. Substitution: Added partyElection.
5. Covered Transactions: Merged exposure and legacyExposure.
6. Updated descriptions where necessary.

_Review Directions_

Changes can be reviewed in PR: [#4081](https://github.com/finos/common-domain-model/pull/4081)
