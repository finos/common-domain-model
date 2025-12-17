# *Ingestion Framework for FpML - Mapping Coverage: Equity Swap Transaction Supplement*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps the Price field for Fixed Rate Schedules, as per [#4303](https://github.com/finos/common-domain-model/issues/4303).

Updates to the mapping of `EquitySwapTransactionSupplement` for:

- ReturnTerms
- DividendReturnTerms
- ValuationDates

_Review Directions_

Changes can be reviewed in PR: [#4304](https://github.com/finos/common-domain-model/pull/4304)