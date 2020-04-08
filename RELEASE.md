# *Model Optimisation: Price Refactor*

_What is being released_

Clean up tasks following the recent model price refactor:

- Migrate regulation reporting rules for `ESMA MiFIR RTS22` to use new price model.

_Review Directions_

In the Textual Browser, review reporting rules:

- `FixedRatePrice`, `FloatingRatePrice`, `FixedFixedPrice` 
- `FixedFloatBuyerSeller`, `CrossCurrencySwapBuyerSeller`, `FixedFixedBuyerSeller`
- `IsFixedFloat`, `IsFixedFixed`, `IsIRSwapBasis`, `IsXCCYSwap`, `IsCreditDefaultSwap`
