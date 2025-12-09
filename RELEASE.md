# *FpML Ingest - Volatility Swap Transaction Supplement*

_Background_

Ingest functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps Volatility Swap Transaction Supplement products, as per [#4261](https://github.com/finos/common-domain-model/issues/4261).

Updates to `MapVolatilityLegToPerformancePayout` function to map:
- priceQuantity
- settlementTerms
- observationTerms
- valuationDates
- returnTerms

_Review Directions_

Changes can be reviewed in PR: [#4264](https://github.com/finos/common-domain-model/pull/4264)