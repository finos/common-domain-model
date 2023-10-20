# Observable Model - Add Repo Purchaseprice and Repurchaseprice Reference

_Background_

`PriceSchedule` does not include a reference to the price type needed to identify the pruchase (initial) price and repurchase (final price) of a repo.
`effectiveDate` and `terminationDate` are defined by `AdjustableOrRelativeDate` but this data type does not include a date reference that is needed to related it to
a contract, termsheet or legal agreement.

_What is being released?_

This enhancement adds the following:

`priceScheduleReference` to `PriceSchedule` and `PriceScheduleReferenceEnum` values `PurchasePrice` and `RepurchasePrice`. These values can
be set in the `tradeLot` for cash or asset price types and in `payout` to define `priceQuantity`.

`dateReference` defined by `DateReferenceEnum` to `AdjustableOrRelativeDate` that will be used in `economicTerms` to define pruchase date and repruchase date using 
enums `PurchaseDate` and `RepurchaseDate`.

