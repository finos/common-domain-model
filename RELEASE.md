
# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependencies.

Version updates include:
- DSL 9.55.0: no relevant changes for the CDM. See DSL release notes: [DSL 9.55.0](https://github.com/finos/rune-dsl/releases/tag/9.55.0)
- DSL 9.56.0: infrastructure optimization for Rosetta. See DSL release notes: [DSL 9.56.0](https://github.com/finos/rune-dsl/releases/tag/9.56.0)

_Review Directions_

There are no changes to the test expectations.

The changes can be reviewed in PR: [#3790](https://github.com/finos/common-domain-model/pull/3790) 

# _Legal Documentation - Descriptions for Migrated Components_

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

All ISDA Foundations components have since been migrated to CDM as part of issue [#3348](https://github.com/finos/common-domain-model/issues/3348), with all ISDA legal IP scrubbed from components and hidden behind a docReference tag. As part of the migration, some descriptions were erroneously removed, while some components did not have descriptions to begin with.

_What is being released?_

This release adds descriptions to the recently migrated components. Some descriptions were reinstated after being removed during migration, while others are new and provided by ISDA. Updated components and their attributes:
- `AccessConditionsElections`
- `ConditionsPrecedent`
- `ControlAgreementNecEvent`
- `ControlAgreementNecEventElection`
- `CreditSupportObligationsVariationMargin`
- `CustodyArrangements`
- `IneligibleCreditSupport`
- `MarginApproach`
- `OtherAgreements`
- `RegimeTerms`
- `SecurityProviderRightsEvent`
- `SecurityProviderRightsEventElection`
- `Substitution`
- `CSAMinimumTransferAmount`
- `PostedCreditSupportItemAmount`
- `UndisputedAdjustedPostedCreditSupportAmount`
- `CreditSupportAmount`

This release also amends formatting and punctuation on all descriptions following feedback on the previous PR [#3693](https://github.com/finos/common-domain-model/pull/3693).

_Review Directions_

Changes can be reviewed in PR: [#3774](https://github.com/finos/common-domain-model/pull/3774)