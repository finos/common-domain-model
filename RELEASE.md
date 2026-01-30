# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` , `bundle` and `FpML as Rune` dependency:

Version updates include:
- `DSL` `9.75.3` Performance improvements and bug fix. See DSL release notes: [9.75.3](https://github.com/finos/rune-dsl/releases/tag/9.75.3)
- `bundle` `11.108.0` Performance improvements and bug fix.
- `FpML as Rune` `1.5.0` See Release notes: [1.5.0](https://github.com/rosetta-models/rune-fpml/releases/tag/1.5.0).
- `FpML as Rune` `1.4.0` See Release notes: [1.4.0](https://github.com/rosetta-models/rune-fpml/releases/tag/1.4.0).

Usages of legacy label syntax have been removed.  No changes to the test pack expectations.

_Review Directions_

The changes can be reviewed in PR: [#4391](https://github.com/finos/common-domain-model/pull/4391) && [#4410](https://github.com/finos/common-domain-model/pull/4410) 

# *Ingestion Framework for FpML - Principal Payment Schedule*

_Background_

An issue was identified related to the FpML mapping of `PrincipalPaymentSchedule` for single final payments. For further information, see [#4410](https://github.com/finos/common-domain-model/issues/4410).

_What is being released?_

Synonym Ingest and Ingest Functions related to `PrincipalPaymentSchedule` have been updated to set `principalPaymentSchedule->finalPrincipalPayment` when `principalPayment->finalPayment` is true.

_Review Directions_

Changes can be reviewed in PR: [#4401](https://github.com/finos/common-domain-model/pull/4401)
