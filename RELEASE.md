# *Product Model: Equity Swap Modelling*

_What is being released_

Fixes for Equity Swap model, and corresponding FpML synonym mappings.

- `valuationDates.periodicDates` - `EquityValuation.valuationDates` previously only allowed list of dates, rather than named date attributes e.g. `calculationStartDate`, `calculationEndDate`, `calculationPeriodFrequency` etc.
- `fxFeature` - added to `EquityPayout`.
  
_Review Directions_

In the Ingestion Panel, review sample:

- `products > equity > eqs-ex12-on-european-index-underlyer-short-form.xml`
