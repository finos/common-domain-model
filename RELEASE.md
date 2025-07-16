# _Collateral Eligibility Enumerations - New options to the EquityTypeEnum and ConcentrationLimitType Enum_

_Background_

Depositary receipts are not currently in CDM but are used in collateral schedules. The Collateral Model would benefit from the addition of Depositary Receipts to collateral criteria, and this contribution adds them to the `Security` type as a type of equity.

This release contains enhancements to the Eligible Collateral representation based upon the work undertaken in 2024 by ISLA to support collateral schedules used for securities lending in the CDM.

_What is being released?_

There are two enumerated lists being enhanced:

1. Added a new `ConvertiblePreference` item to the `EquityTypeEnum`
2. Added a new `OutstandingBalance` item to the `ConcentrationLimitTypeEnum`

_Review Directions_

Changes can be reviewed in PR: [3898](https://github.com/finos/common-domain-model/pull/3898)
