# *Model Optimisation: Price Refactor*

_What is being released_

Clean up tasks following the recent model price refactor:

- Migrate conditions related FpML rule IRD 25 to type `TradableProduct` - `FpML_ird_25_FixedRate`, `FpML_ird_25_FloatingRateSpread`, `FpML_ird_25_FloatingRateCapRate` and `FpML_ird_25_FloatingRateFloorRate`.
- To support use case for a floating rate multiplier schedule, add attribute `multiplier` to type `FloatingInterestRate`, and add FpML sample `ird-ex27-inverse-floater.xml`. 
- Migrate condition `FloatingRateMultiplierSchedule` to type `FloatingInterestRate`.

_Review Directions_

In the Textual Browser, review:
- Conditions `FpML_ird_25_FixedRate`, `FpML_ird_25_FloatingRateSpread`, `FpML_ird_25_FloatingRateCapRate` and `FpML_ird_25_FloatingRateFloorRate` on type `TradableProduct`. 
- Condition  `FloatingRateMultiplierSchedule` on type `FloatingInterestRate`.

In the Ingestion Panel, review sample `products > rates > ird-ex27-inverse-floater.xml`.
