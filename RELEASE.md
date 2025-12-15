# *FpML Ingest - Fixed Rate Price*

_Background_

Ingest functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps the Price field for Fixed Rate Schedules, as per [#4262](https://github.com/finos/common-domain-model/issues/4276).

Updates to mapping of `FixedRateSchedule` to now map the `PRice` field.

_Review Directions_

Changes can be reviewed in PR: [#4285](https://github.com/finos/common-domain-model/pull/4285)