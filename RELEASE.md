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

# _Collateral Model - Adding DepositaryReceipt to EquityTypeEnum and creating DepositaryReceipt enum_

_Background_

Depositary receipts are not currently in CDM but are used in collateral schedules. The Collateral Model would benefit from the addition of Depositary Receipts to collateral criteria, and this contribution adds them to the `Security` type as a type of equity.

Due to the current `Security` structure, the `equityType` attribute must be remodelled to allow for the addition of Depositary Receipts.

Depositary Receipts can be of types American, European, Global, and Indian.

_What is being released?_

- Created a new type `EquityType` with two attributes: `EquityTypeEnum` and `DepositaryReceiptTypeEnum`
- Created a new enum: `DepositaryReceiptTypeEnum`
- Added `DepositaryReceipt` to `EquityTypeEnum` (Enum already contains Ordinary & NonConvertiblePreference)
- Added a condition that `DepositaryReceiptEnum` is absent if `DepositaryReceipt` is not selected as the equity type

_Review Directions_

Changes can be reviewed in PR: [3883](https://github.com/finos/common-domain-model/pull/3883)
