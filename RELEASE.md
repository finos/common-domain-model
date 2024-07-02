# _Reference Data - Update ISOCurrencyCodeEnum_

_What is being released?_

Updated `ISOCurrencyCodeEnum` based on updated scheme ISO Standard 4217.

Version updates include:
- added value: `ZWG`


_Review directions_

The changes can be reviewed in PR: [#3018](https://github.com/finos/common-domain-model/pull/3018)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.12.0: this release fixes an issue where the `only exists` operator behaved unexpectedly when subtyping was involved. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.0.
- `DSL` 9.12.1: this patch fixes null pointers in the Java runtime of the `only exists` operator. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.1.
- `DSL` 9.12.2: this patch fixes a code generation bug in the Java generator. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.2.
- `DSL` 9.12.3: this patch fixes an issue where the code generator could freeze Rosetta. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.3.

_Review Directions_

The changes can be reviewed in PR: [#3006](https://github.com/finos/common-domain-model/pull/3006)