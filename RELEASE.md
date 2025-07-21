# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.57.0 Measure and log build statistics, see DSL release notes: [DSL 9.57.0](https://github.com/finos/rune-dsl/releases/tag/9.57.0)
- `DSL` 9.58.0 Change detection fixes and performance improvements, see DSL release notes: [DSL 9.58.0](https://github.com/finos/rune-dsl/releases/tag/9.58.0)
- `DSL` 9.58.1 Patch Validator class for backward compatibility, see DSL release notes: [DSL 9.58.1](https://github.com/finos/rune-dsl/releases/tag/9.58.1)
- `DSL` 9.59.0 Continued migrating to Xtend and fixed null pointer issues in generated function code and type alias condition code, see DSL release notes: [DSL 9.59.0](https://github.com/finos/rune-dsl/releases/tag/9.59.0)
- `DSL` 9.60.0 Migration to Maven Central, see DSL release notes: [DSL 9.60.0](https://github.com/finos/rune-dsl/releases/tag/9.60.0)
- `DSL` 9.61.0 Fixed issues with type coercion and location setting, added support for using ^type, class, result, and package as attribute names, and improved the with-meta operation to work with empty arguments, see DSL release notes: [DSL 9.61.0](https://github.com/finos/rune-dsl/releases/tag/9.61.0)


There are no changes to model or test expectations.  The allocated memory configured to run the tests has been increased to prevent failures when running tests.

_Review Directions_

The changes can be reviewed in PR: [#3900](https://github.com/finos/common-domain-model/pull/3900)

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
