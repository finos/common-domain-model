# *Model Optimisation: Price Refactor*

_What is being released_

Clean up tasks following the recent model price refactor:

- Add FpML synonym mappings logic to populate `Schedule.initialValue` when a price schedule exists.
- Rename type `InterestRateSpread` to `FloatingInterestRate`.
- Add `[deprecated]` annotation to deprecated attributes.
- Fix minimum cardinality for `TradableProduct.priceNotation`.

_Review Directions_

In the Ingestion Panel, try the following samples which contain price step schedules in folder `products > rates`:

- `EUR-variable-notional-uti.xml`
- `ird-ex04-arrears-stepup-fee-swap-usi-uti.xml`
- `ird-ex22-cap.xml`
- `ird-ex23-floor.xml`
- `ird-ex24-collar.xml`
- `GBP-VNS-uti.xml`
- `USD-VNS-uti.xml`

