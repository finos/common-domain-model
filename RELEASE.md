# Observable - Addition of price sub types

_Background_

The `PriceSchedule` offers a `priceType` which can be used to specify what type of price this is e.g. InterestRate, AssetPrice. In certain scenarios more granularity is required.

For example, when defining a price as an "InterestRate" on a securities lending trade, we need to be able to further identify if this is a lending fee or a rebate rate.

_What is being released?_

The key changes are:

- A new `PriceSubTypeEnum` has been created that holds the same options as `CashPriceTypeEnum` but with the addition of the "Rebate" option.
- A new optional attribute `priceSubType` of type `PriceSubTypeEnum` has been added to `PriceSchedule`
- The `PriceSchedule -> cashPrice` attribute has been removed as it is not required in the context of a `PriceSchedule`.
- The `premiumType` attribute, previously under `cashPrice -> premiumExpression`, has been added directly under `PriceSchedule`.
- The functions that create new `Price` items have been updated to remove `cashPrice` and include `priceSubType`
- The functions updated were:
  - Create_StockSplit
  - Create_OnDemandRateChangePriceChangeInstruction
  - Create_RepricePrimitiveInstruction
  - Create_AdjustmentPrimitiveInstruction
  - ResolveEquityInitialPrice
- The Ingest functions have been updated to reflect the changes too.

Note that the existing `CashPrice` and `CashPriceTypeEnum` are still used by transfers. They will remain in the model for now and their usage will be reviewed further in [#4195](https://github.com/finos/common-domain-model/issues/4195)

Extensive analysis and review was performed in order to define these changes. For full details please refer to the Issue [#3871](https://github.com/finos/common-domain-model/issues/3871)

_Review Directions_

Changes can be reviewed in PR: [#4218](https://github.com/finos/common-domain-model/pull/4218)