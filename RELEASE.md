# *CDM - Eligible Collateral condition logic*

_Background_

A recent enhancement to the validation in the Rune DSL has highlighted some logic defects in the conditions that are applied on `EligibleCollateralCriteria`. This release does not change the functionality but ensures that the logic will work correctly in all possible scenarios.

_What is being released?_

The following three conditions were impacted:
- ConcentrationLimitTypeIssueOSAmountDebtOnly
- ConcentrationLimitTypeMarketCapEquityOnly
- AverageTradingVolumeEquityOnly

The fixes ensure that the logic works correctly when there are multiple concentration limits.

_Review Directions_

The changes can be reviewed in PR: [#3326](https://github.com/finos/common-domain-model/pull/3326).
