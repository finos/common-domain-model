# _Collateral Model - Updates to EquityTypeEnum and ConcentrationLimitTypeEnum_

_Background_

In 2024, ISLA began development to support collateral schedules for securities lending within the Common Domain Model.

_What is being released?_

This release contains enhancements to the Eligible Collateral representation based upon this work, with new options made available in existing enumerated lists.

There are two enumerated lists being enhanced:

1. Added option `ConvertiblePreference` to `EquityTypeEnum`
2. Added option `OutstandingBalance` to `ConcentrationLimitTypeEnum`

_Review Directions_

Changes can be reviewed in PR: [3912](https://github.com/finos/common-domain-model/pull/3912)

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
