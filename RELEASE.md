# *Ingest - Mapping Volatility Swap Transaction Supplement*

_Background_

Ingest functions for FpML Confirmation to CDM are available still have gaps between the original synonym mapping and ingest functional mapping.

_What is being released?_

This release maps Volatility Swap Transaction Supplement products.

Updates to `MapVolatilityLegToPerformancePayout` function to map:
- priceQuantity
- settlementTerms
- observationTerms
- valuationDates
- returnTerms

_Review Directions_

Changes can be reviewed in PR: [#4255](https://github.com/finos/common-domain-model/pull/4255)