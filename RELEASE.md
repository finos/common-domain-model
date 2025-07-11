# _Collateral Model - Adding DepositoryReceipt to EquityTypeEnum and creating DepositoryReceipt enum_

_Background_

Depository receipts are not currently in CDM but are used in collateral schedules. The Collateral Model would benefit from the addition of Depository Receipts to collateral criteria, and this contribution adds them to the `Security` type as a type of equity.

Due to the current `Security` structure, the `equityType` attribute must be remodelled to allow for the addition of Depository Receipts.

Depository Receipts can be of types American, European, Global, and Indian.

_What is being released?_

- Created a new type `EquityType` with two attributes: `EquityTypeEnum` and `DepositoryReceiptTypeEnum`
- Created a new enum: `DepositoryReceiptTypeEnum`
- Added `DepositoryReceipt` to `EquityTypeEnum` (Enum already contains Ordinary & NonConvertiblePreference)
- Added a condition that `DepositoryReceiptEnum` is absent if `DepositoryReceipt` is not selected as the equity type

_Review Directions_

Changes can be reviewed in PR: [3883](https://github.com/finos/common-domain-model/pull/3883)
# _CDM Distribution - Python Code Generation flag update_

_Background_

After an upgrade to using xtext, the incremental builds were running with value `true`. This resulted in the Python build not generating the expected number of enum files, as detailed in [Issue 3829](https://github.com/finos/common-domain-model/issues/3829).

_What is being released?_

Configuration changes have been made to set `incrementalXtextBuild` to `false` to ensure enum files are generated as expected.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3862

