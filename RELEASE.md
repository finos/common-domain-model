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

# *Model Optimisation: Increase readability of condition names and simply the path expressions*

_What is being released_

- The names of all of the type conditions in the CDM have been renamed to increase readability.
- The paths defined in the conditions no longer need to start with the type of the condition. This greatly increases the readability of the conditions.

_Review Directions_

- Navigate to the `Frequency` type and see the condition named `PositivePeriodMultiplier`. This was previously named `Frequency_periodMultiplier`.

- See that the path that the path is simpliefied:
  - Before: `if Frequency -> period = PeriodExtendedEnum -> T then Frequency -> periodMultiplier = 1`
  - After: `if period = PeriodExtendedEnum -> T then periodMultiplier = 1`
  
 All conditions throughout the CDM have been updatged in similar ways.
 
