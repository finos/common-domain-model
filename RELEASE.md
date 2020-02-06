# *Model Optimisation: Exchange Rate and Quantity Data Rule*

_What is being released_

- Add data rule for FX products that checks triangulation between `ExchangeRate` and the payout `Quantity` for each currency.
- A number of DSL fixes and stability improvements relating to code generation of conditional logic.

_Review Directions_

In the Textual Browser, review the following:

- Functions: `PriceQuantityTriangulation`, `ExchangeRateQuantityTriangulation`
- Data rules: `ContractPriceQuantityTriangulation`, `ExecutionPriceQuantityTriangulation`
 
In the Ingestion Panel, try one of the following samples:
- `products > fx > fx-ex01-fx-spot.xml`
- `products > fx > fx-ex02-spot-cross-w-side-rates.xml`
- `products > fx > fx-ex03-fx-fwd.xml`
- `products > fx > fx-ex04-fx-fwd-w-settlement.xml`
- `products > fx > fx-ex05-fx-fwd-w-ssi.xml`
- `products > fx > fx-ex06-fx-fwd-w-splits.xml`
- `products > fx > fx-ex07-non-deliverable-forward.xml`
- `products > fx > fx-ex28-non-deliverable-w-disruption.xml`