# *Ingestion Framework for FpML - Mapping Coverage: Credit Default Swaption Underlier*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps the Underlier field for Credit Default Swaption products, as per [#4289](https://github.com/finos/common-domain-model/issues/4289).

Updates to mapping of `CreditDefaultSwapOption` to now map the `underlier` field.

_Review Directions_

Changes can be reviewed in PR: [#4291](https://github.com/finos/common-domain-model/pull/4291)