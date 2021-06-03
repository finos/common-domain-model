# *Product Model - Observable attribute references*

_What is being released?_

Following the recent price, quantity and observable refactor, this release makes the `PriceQuantity->observable` attributes referencable by adding `location`/`address` annotations to the attributes of type `Observable` and their corresponding `Product` attributes.  Synonyms have also been migrated to the new model for all products including rates, equity, FX, credit and repo, and also all other Event, DTCC and CME synonyms.

- Add `location` annotation to `Observable->commodity` and add `address` annotation to `CommodityPayout->underlier->commodity`.
- Add `location` annotation to `Observable->productIdentifier` and add `address` annotation to `PayoutBase->productIdentifier` (super type of `Security`, `Loan` and `Index`).
- Add `location` annotation to `Observable->currencyPair` and add `address` annotation to:
  - `optionPayout.exerciseTerms.settlement.fxSettlementTerms.fixing.quotedCurrencyPair`
  - `forwardPayout.settlementTerms.fxSettlementTerms.fixing.quotedCurrencyPair`
  - `optionPayout.feature.averagingRateFeature.fxRateObservable.quotedCurrencyPair`
- Add `deprecated` annotation to ExchangeRate type.

_Review Directions_

In the CDM Portal, select the textual browser and inspect the types mentioned above.

In the CDM Portal, select ingestion and review the following samples:

`Observable->commodity`:
- fpml-5-10/products/commodity/com-ex1-gas-swap-daily-delivery-prices-last
- fpml-5-10/products/commodity/com-ex5-gas-v-electricity-spark-spread
- fpml-5-10/products/commodity/com-ex8-oil-call-option-strip

`Observable->productIdentifier`:
- fpml-5-10/products/rates/bond-option-uti
- fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json (Security)
- fpml-5-10/products/equity/eqd-ex04-european-call-index-long-form.json (Index)

`Observable->currencyPair`:
- fpml-5-10/products/equity/fx-ex07-non-deliverable-forward
- fpml-5-10/products/equity/fx-ex11-non-deliverable-option
- fpml-5-10/products/equity/fx-ex22-avg-rate-option-specific


# *DSL Syntax - Deprecation of "includes" keyword*

_What is being released?_

The use of the keyword `includes` has been deprecated in favor of the equivalent keyword`contains`. The former was only used in the definition of the function `Create_ClearedTrade`.

_Review Directions_

In the CDM Portal, select the textual browser and inspect that the syntax keyword `includes` is no longer present.
