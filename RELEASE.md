# *Product Model - Updating Qualification Functions to Handle `only exists` Syntax*

_Background_

In 7 dev, the `only exists` syntax does not apply to the choice `Payout -> SettlementPayout`, because there is always only one. Instead, `only-element` is used on the payout, which is incorrect, because if there is more than one payout then none will be set. The original intention was to allow for multiple of the same payout types.

_What is being released?_

Updating any previous instance of `only-exist` to use a function which checks whether only the payout in questions exists, allowing for multiple of the same payouts.

_Review Directions_

Changes can be reviewed in PR: [#4415](https://github.com/finos/common-domain-model/pull/4415)

# *Product Model - EquityForward Qualification functions*

_Background_

There are no qualification functions for Equity Forwards.

_What is being released?_

Qualification Functions for Equity Forwards introduced:
- `Qualify_EquityForward_PriceReturnBasicPerformance_SingleName` 
- `Qualify_EquityForward_PriceReturnBasicPerformance_SingleIndex` 
- `Qualify_EquityForward_PriceReturnBasicPerformance_Basket`

_Review Directions_

The changes can be reviewed in PR: [#4405](https://github.com/finos/common-domain-model/pull/4405)

# *Ingestion Framework for FpML - Mapping Coverage: FX and Rates*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage. For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps FX and Rates products, as per [#4373](https://github.com/finos/common-domain-model/issues/4373) and [#4440](https://github.com/finos/common-domain-model/issues/4440).

- Mapping updates to `quantitySchedule` for FpML FX products
- Duplicate mappings removed in product taxonomy for FpML FRA products

_Review Directions_

Changes can be reviewed in PR: [#4376](https://github.com/finos/common-domain-model/pull/4376)
