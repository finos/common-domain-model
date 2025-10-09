# Ingest & Observable - Continued Mapping for creditdefaultswap 

_Background_

Ingest functions for FpML Confirmation to CDM are available in the CDM 7-dev version, where they are available for beta testing by the CDM community. There are still gaps in the original synonym mapping and ingest functional mapping which will be addressed in this PR.

_What is being released?_

Adding additional code for a number of different boolean fields to be mapped. These have been added under:

- `creditdefaultswap` under `ingest`
- `event` under `observable`

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/4073

# BrokerEquityOption mappings for fpml ingest

_Background_

BrokerEquityOption mappings for fpml ingest

_What is being released?_

BrokerEquityOption mappings for fpml ingest

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/4088

# CDM Fpml Ingest - Vanilla Equity Option Synonym to Fpml Ingest Function Mapping fixes

_Background_

Ingest functions for FpML Confirmation to CDM are available in the CDM 7-dev version, where they are available for beta testing by the CDM community. There are still gaps in the original synonym mapping and ingest functional mapping which will be addressed in this PR.

_What is being released?_

Mapped the following:

`common` :

- `expirationTimeType` and `multipleExercise` for `BermudaExercise`
datetime
- `dateTimeList` to be populated with a zoned date time pricequantity
- `financialUnit` to populate correct values opposed to being hardcoded to `Contract`
`equityoption`
- `averagingFeature` and `Passthrough` under `feature`

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/4090

# Ingest - Mapping FpML Events to WorkflowStep

_Background_

Ingest functions for FpML Confirmation to CDM are available in the CDM 7-dev version for beta testing by the CDM community. There are still gaps in the original synonym mapping and ingest functional mapping which will be addressed in this PR.

_What is being released?_

Added new mapping for events in WorkflowStep

- PrimitiveInstruction for different message types
- Event timestamps
- Message information
- Event actions

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/4077