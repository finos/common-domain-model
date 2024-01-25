# _Product Model - FpML synonym mappings for Price KnownAmountSchedule_

_Background_

This release resolves FpML mappings for KnownAmountSchedule component which was mapped into the TradeLot and not represented into the PriceSchedule of the corresponding Payout .The `PriceQuantity>priceSchedule` component is not represented in the InterestRatePayout when we have a sample with `knownAmountSchedule`. This release updates the mappings so that knownAmountSchedule is mapped to the `priceSchedule` component.

_What is being released?_

- Updated mappings for `PriceSchedule` to FpML `knownAmountSchedule` for Rates.

_Review directions_

In CDM portal select the following samples:

fpml-5-10 / products / rates
- IR IRS FixedFloat ZC KnownAmount

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR [#2644](https://github.com/finos/common-domain-model/pull/2644)

