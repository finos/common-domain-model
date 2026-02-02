# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` and `bundle` dependency:

Version updates include:
- `DSL` `9.75.3` Performance improvements and bug fix. See DSL release notes: [9.75.3](https://github.com/finos/rune-dsl/releases/tag/9.75.3)
- `bundle` `11.108.0` Performance improvements and bug fix.

_Review Directions_

The changes can be reviewed in PR: [#4409](https://github.com/finos/common-domain-model/pull/4409)

# Product Model - Barrier Options Cardinality Updates

_Background_

Barrier Options can have multiple knock-ins and knock-outs which are not supported with the current cardinality. The cardinality of the `knockIn` or `knockOut` / `barrierCap` or `barrierFloor` attributes is currently `(0..1)`.

_What is being released?_

Relaxing the cardinality to `(0..*)` to handle multiple `knockIn` or `knockOut` / `barrierCap` or `barrierFloor`.

_Review Directions_

Changes can be reviewed in PR: [#4357](https://github.com/finos/common-domain-model/pull/4357)

# *Ingestion Framework for FpML - Principal Payment Schedule*

_Background_

An issue was identified related to the FpML mapping of `PrincipalPaymentSchedule` for single final payments. For further information, see [#4076](https://github.com/finos/common-domain-model/issues/4076).

_What is being released?_

Synonym Ingest mappings related to `PrincipalPaymentSchedule` have been updated to set `principalPaymentSchedule->finalPrincipalPayment` when `principalPayment->finalPayment` is true.

_Review Directions_

Changes can be reviewed in PR: [#4402](https://github.com/finos/common-domain-model/pull/4402)