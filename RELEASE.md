# _Reference Data - Update ISOCurrencyCodeEnum_

_What is being released?_

Updated `ISOCurrencyCodeEnum` based on updated scheme ISO Standard 4217.

Version updates include:
- Removed values: `MWK`, `PEN`, `RON`, `SZL`, `TRY`
- Added value: `ZWG`

_Backward Incompatible Changes_

As this release removes multiple enum values, it will not be backwards compatible.

_Review directions_

The changes can be reviewed in PR: [#3014](https://github.com/finos/common-domain-model/pull/3014)
