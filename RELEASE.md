# *Ingest - Mapping FpML Events to WorkflowStep*

_Background_

Ingest functions for FpML Confirmation to CDM are available in the CDM 7-dev version for beta testing by the CDM community. There are still gaps in the original synonym mapping and ingest functional mapping which will be addressed in this PR.

_What is being released?_

Added new mapping for events in `WorkflowStep`
- `PrimitiveInstruction` for different message types
- Event timestamps
- Message information
- Event actions

_Review Directions_

Changes can be reviewed in PR: [#4082](https://github.com/finos/common-domain-model/pull/4082)