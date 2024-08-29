# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency. 

Version updates include:
- `DSL` 9.16.2: support for ingestion tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.1

_Review directions_

The changes can be reviewed in PR: [#3107](https://github.com/finos/common-domain-model/pull/3107)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency. 

Version updates include:
- `DSL` 9.15.0: patch for supporting tabulation of types with circular dependencies. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.0
- `DSL` 9.15.1: patch for missing generated Java files. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.1
- `DSL` 9.15.2: patch for missing Java meta classes. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.2

_Review directions_

The changes can be reviewed in PR: [#3098](https://github.com/finos/common-domain-model/pull/3098)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `ingest-test-framework` dependency.

Version updates include:
- `ingest-test-framework` 11.17.1: Add support for address/location references to be used on nested model types.

FpML Java mapping code that sets address/location references have been updated accordingly.  There are no changes to the model. 

_Review directions_

The price referencing has been fixed in the following sample:

- fpml-5-13 / products / interest-rate-derivatives / ird-ex37-zero-coupon-swap-known-amount-schedule.xml

The changes can be reviewed in PR: [#3100](https://github.com/finos/common-domain-model/pull/3100)
