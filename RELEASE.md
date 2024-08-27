# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `ingest-test-framework` dependency.

Version updates include:
- `ingest-test-framework` 11.17.1: Add support for address/location references to be used on nested model types.

FpML Java mapping code that sets address/location references have been updated accordingly.  There are no changes to the model. 

_Review directions_

The price referencing has been fixed in the following sample:

- fpml-5-13 / products / interest-rate-derivatives / ird-ex37-zero-coupon-swap-known-amount-schedule.xml

The changes can be reviewed in PR: [#3099](https://github.com/finos/common-domain-model/pull/3099)
