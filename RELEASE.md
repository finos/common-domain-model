# *Product Model – 2021 Floating Rate Definitions*

_What is being released?_

The floating rate structure has been extended to include components from the new 2021 ISDA definitions, including new floating rate calculation parameters (observation shift, lookback, lockout, cap/floor) and fallback parameters. The new structure has been validated against FpML 5.12 samples that include the new definitions.

_Details_

- `FloatingRateCalculationParameters` describes the parameters applicable to the new floating rate calculations.
- New enumerations have been introduced to describe the calculation method:

  - `CalculationMethodEnum`
  - `ObservationPeriodDatesEnum`
  - `CalculationShiftMethodEnum`

- `FallbackRateParameters` describes the fallback parameters, including the relevant calculation parameters for the fallback rate.
- New `calculationParameters` and `fallbackRate` attributes have been added to the `FloatingRate` data type
- A new FpML 5.12 example: `ird-ex40-rfr-avg-swap-obs-period-shift` has been added to the ingestion pack, that tests an example of the new structure

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above.

Select the Ingestion view and review the above sample.
