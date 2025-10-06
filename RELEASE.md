# *Ingest & observable - Continued Mapping for creditdefaultswap*

_Background_

Ingest functions for FpML Confirmation to CDM are available in the CDM 7-dev version, where they are available for beta testing by the CDM community. There are still gaps in the original synonym mapping and ingest functional mapping which will be addressed in this PR.
_What is being released?_

Adding additional code for a number of different `boolean` fields to be mapped. These have been added under:

- `creditdefaultswap` under `ingest`
- `event` under `observable`
- `settlement` under `ingest`

_Review Directions_

Changes can be reviewed in PR: [#4074](https://github.com/finos/common-domain-model/pull/4074)
