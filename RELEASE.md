# _Product Model - Equity Products_

_What is being released?_

This issue was discusses at the CDM Derivatives Products and Business Events Working Group on August 14th, 2024 #3077 and resolves items 1, 2 and 4 from Issue #3087. Item 3 was discussed and will be contributed at a later time.

- Item 1: Zero Strike option
In `PriceSchedule`, `Positive Price` condition requires `Cash Price` to be greater than `0`. This caused a CDM error for Zero Strike Option products.
The resolution to this is to relax the `Positive Price` condition to allow to 0.

- Item 2: `Quantity` Condition in `Payout`
In type `Payout`, `Quantity` condition expects a `priceQuantity` or `interestRatePayout` legs only. For FX Options, there’re no `interestRatePayout` legs, hence this condition throws an error for FX Options.
The resolution to this is to add `foreignExchange` check in condition `Quantity`.

- Item 4: Equity Forwards Qualification
Added following Qualification functions:
 - `Qualify_EquityForward_PriceReturnBasicPerformance_SingleName`
 - `Qualify_EquityForward_PriceReturnBasicPerformance_SingleIndex`
 - `Qualify_EquityForward_PriceReturnBasicPerformance_Basket`

_Review directions_

The changes can be reviewed in PRs: [#3092](https://github.com/finos/common-domain-model/pull/3092).
