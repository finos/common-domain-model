# *Product Model - Updating Qualification Functions to Handle `only exists` Syntax*

_Background_

In 6.x.x, the `only exists` syntax does not apply to the choice `Payout -> SettlementPayout`, because there is always only one. Instead, `only-element` is used on the payout, which is incorrect, because if there is more than one payout then none will be set. The original intention was to allow for multiple of the same payout types.

_What is being released?_

Updating any previous instance of `only-exist` to use a function which checks whether only the payout in questions exists, allowing for multiple of the same payouts.

_Review Directions_

Changes can be reviewed in PR: [#4407](https://github.com/finos/common-domain-model/pull/4407)

# _Infrastructure - Dependency Update_

_What is being released?_

This change updates the version of the `FpML as Rune` dependency to version 1.4.0.

Version updates include:
- `FpML as Rune` `1.4.0` See Release notes: [1.4.0](https://github.com/rosetta-models/rune-fpml/releases/tag/1.4.0).

_Review Directions_

The changes can be reviewed in PR: [#4391](https://github.com/finos/common-domain-model/pull/4391)

# *Ingestion Framework for FpML - Principal Payment Schedule*

_Background_

An issue was identified related to the FpML mapping of `PrincipalPaymentSchedule` for single final payments. For further information, see [#4076](https://github.com/finos/common-domain-model/issues/4076).

_What is being released?_

Synonym Ingest and Ingest Functions related to `PrincipalPaymentSchedule` have been updated to set `principalPaymentSchedule->finalPrincipalPayment` when `principalPayment->finalPayment` is true.

_Review Directions_

Changes can be reviewed in PR: [#4401](https://github.com/finos/common-domain-model/pull/4401)
