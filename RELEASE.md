# *Product Model - EquityForward Qualification functions*

_Background_

There are no qualification functions for Equity Forwards.

_What is being released?_

Qualification Functions for Equity Forwards introduced:
- `Qualify_EquityForward_PriceReturnBasicPerformance_SingleName` 
- `Qualify_EquityForward_PriceReturnBasicPerformance_SingleIndex` 
- `Qualify_EquityForward_PriceReturnBasicPerformance_Basket`

_Review Directions_

Changes can be reviewed in PR: [#4404](https://github.com/finos/common-domain-model/pull/4404)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` , `bundle` and `FpML as Rune` dependency:

Version updates include:
- `DSL` `9.75.3` Performance improvements and bug fix. See DSL release notes: [9.75.3](https://github.com/finos/rune-dsl/releases/tag/9.75.3)
- `bundle` `11.108.0` Performance improvements and bug fix.
- `FpML as Rune` `1.5.0` See Release notes: [1.5.0](https://github.com/rosetta-models/rune-fpml/releases/tag/1.5.0).
- `FpML as Rune` `1.4.0` See Release notes: [1.4.0](https://github.com/rosetta-models/rune-fpml/releases/tag/1.4.0).

_Review Directions_

The changes can be reviewed in PR: [#4391](https://github.com/finos/common-domain-model/pull/4391) && [#4410](https://github.com/finos/common-domain-model/pull/4410) 

# *Ingestion Framework for FpML - Principal Payment Schedule*

_Background_

An issue was identified related to the FpML mapping of `PrincipalPaymentSchedule` for single final payments. For further information, see [#4076](https://github.com/finos/common-domain-model/issues/4076).

_What is being released?_

Synonym Ingest and Ingest Functions related to `PrincipalPaymentSchedule` have been updated to set `principalPaymentSchedule->finalPrincipalPayment` when `principalPayment->finalPayment` is true.

_Review Directions_

Changes can be reviewed in PR: [#4401](https://github.com/finos/common-domain-model/pull/4401)

# *Ingestion Framework for FpML - Mapping Coverage: FX and Rates*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage. For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps FX and Rates products, as per [#4373](https://github.com/finos/common-domain-model/issues/4373) and [#4440](https://github.com/finos/common-domain-model/issues/4440).

- Updates to mapping of `quantitySchedule` for FpML FX products
- Fixes duplicate mappings of product taxonomy for FpML FRA products

_Review Directions_

Changes can be reviewed in PR: [#4339](https://github.com/finos/common-domain-model/pull/4339)
