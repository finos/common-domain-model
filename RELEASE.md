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


# _Event Model - TradeState Quantity Decrease update_

_Background_

The `QuantityDecreasedToZero` function currently assumes that a decrease has occurred when the quantity is reduced to zero by checking that:

1. The quantity in the after TradeState is zero.
2. The quantity in the before TradeState is zero or greater.

This leads to incorrectly flagging as decreases the cases where both the `before` and `after` quantities are zero, which should not be considered decreases.

_What is being released?_

This contribution modifies the logic in the `QuantityDecreasedToZero` function so that a decrease is only considered when the before quantity is greater than zero, not equal to zero.

_Review Directions_

The changes can be reviewed in PR: [#3759](https://github.com/finos/common-domain-model/pull/3759)
