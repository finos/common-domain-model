# *Product Model - FX Variance and Volatility Swaps*

_Background_

The new `Payout` `performancePayout` was recently introduced to allow for representation of a wider variety of products, not only from the equity asset class, but also from any product involving a return determined by an observation. This release introduces support for the first non-equity products, namely FX variance swaps and FX volatility swaps.

_What is being released?_

- Minor changes to Performance Payout to enable it to accommodate FX variance and FX volatility swaps.
- Mapping coverage for FX variance and FX volatility swaps.
- Minor adjustments to FX qualification functions.

_Functions_

- `dayType` added to `PeriodicDates` type (base-datetime-type)
- `price`removed from `EquityObservation` type (observable-asset-type)
- Type of `boundedCorrelation` changed from `BoundedCorrelation` to `NumberRange` (product-asset-type)
- `numberOfObservationDates` added to `ObservationTerms` type (product-common-schedule-type)
- Unused `AveragingObservation` type removed (product-common-settlement-type)
- Type `performancePayout` moved to product-template-type (product-common-settlement-type)
- `observationTerms` added to `performancePayout`(product-template-type)

_Qualification_

Minor changes to:
- Qualify_ForeignExchange_ParameterReturnVariance
- Qualify_ForeignExchange_ParameterReturnVolatility

_Translate_

Added mapping coverage for FX variance and FX volatility swaps.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

fpml-5-10/incomplete-products/fx-derivatives
- fx-ex30-variance-swap.xml
- fx-ex31-volatility-swap.xml
