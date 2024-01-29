# _Product Model - FpML synonym mappings for Price KnownAmountSchedule_

_Background_

Previously, the `PriceQuantity>priceSchedule` component lacked representation in the InterestRatePayout, specifically when dealing with samples featuring a `knownAmountSchedule`. This gap existed because we were only capturing knownAmountSchedules within the `TradeLot` and not extending that representation to the corresponding `Payout`.

_What is being released?_

In this release, we address the FpML mappings for the `KnownAmountSchedule` component, which was originally mapped to the `TradeLot` but not adequately reflected in the PriceSchedule of the corresponding Payout. The update ensures that the knownAmountSchedule is now appropriately mapped to the `priceSchedule` component.

- Enhanced synonym mappings for `PriceSchedule` have been updated to align with FpML  `knownAmountSchedule` for Rates.

_Review directions_

To assess these changes, navigate to the CDM portal and select the following samples:

fpml-5-10 / products / rates
- IR IRS FixedFloat ZC KnownAmount

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR [#2644](https://github.com/finos/common-domain-model/pull/2644)

