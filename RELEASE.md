# *CDM - Map Equity Swap Transaction Supplement*

_Background_

Fixing mapping issues found with equity swap transaction supplement in an effort to move away from synonyms over to functions. The missing mappings were the following:

- TradeState → trade → product → economicTerms → payout → InterestRatePayout → priceQuantity
  - quantitySchedule

- TradeState → trade → product → economicTerms → payout → InterestRatePayout → rateSpecification → FloatingRateSpecification → spreadSchedule → price →
  - Unit and per unit of MISSING and location different

- TradeState → trade → product → economicTerms → payout → PerformancePayout → returnTerms → dividendReturnTerms
  - firstOrSecondPeriod MISSING

- TradeState → trade → tradeLot → priceQuantity → price
  - Unit and per unit of MISSING and location different

- TradeState → trade
  - Adjustment missing
  
_What is being released?_
Fixes to the missing mappings in order to populate the paths noted under `background`. Fixes are contained in:

returnswap
equityswaptransactionsupplement
tradestate


_Review Directions_

Changes can be reviewed in PR: [#4067](https://github.com/finos/common-domain-model/pull/4067)
