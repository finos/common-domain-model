# Observable Model - Add Repo Purchaseprice and Repurchaseprice Reference

_Background_

`PriceSchedule` does not include a reference to the price type needed to identify the pruchase (initial) price and repurchase (final price) of a repo.

_What is being released?_

This enhancement adds `priceScheduleReference` to `PriceSchedule` and `PriceScheduleReferenceEnum` values `PurchasePrice` and `RepurchasePrice`. These values can
be set in the `tradeLot` for cash or asset price types.
