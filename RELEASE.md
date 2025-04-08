# _Infrastructure - Dependency Update_

_Background_

The Rosetta platform has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

This release updates `ISOCurrencyCodeEnum` to keep it in sync with the latest ISO 4217 coding scheme.
* The following enum value has been added:
  * `XCG <"Caribbean Guilder">`

_Review Directions_

The changes can be reviewed in PR: [#3611](https://github.com/finos/common-domain-model/pull/3611)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.38.0 Fixed setting enum values on meta fields. For further details, see DSL release notes: [DSL 9.38.0](https://github.com/finos/rune-dsl/releases/tag/9.38.0)
- `DSL` 9.39.0 Fixed default operation issue and added support for `with-meta` operation. For further details, see DSL release notes: [DSL 9.39.0](https://github.com/finos/rune-dsl/releases/tag/9.39.0)
- `DSL` 9.40.0 Added support for regulatory reference paths. For further details, see DSL release notes: [DSL 9.40.0](https://github.com/finos/rune-dsl/releases/tag/9.40.0)
- `DSL` 9.40.1 Deprecated `productType`, `eventType`, and `calculation`. For further details, see DSL release notes: [DSL 9.40.1](https://github.com/finos/rune-dsl/releases/tag/9.40.1)
- `DSL` 9.41.0 Enabled support for dynamic validation and injection to improve the configurability of the generated code. The change also included a fix that prevents correct operation of DSL quick actions. For further details, see DSL release notes: [DSL 9.41.0](https://github.com/finos/rune-dsl/releases/tag/9.41.0)
- `DSL` 9.41.1 Annotated POJO attributes to highlight address & locations. Fixed issue in code generation for translate. For further details, see DSL release notes: [DSL 9.41.1](https://github.com/finos/rune-dsl/releases/tag/9.41.1)
- `DSL` 9.42.0 Added support for converting enums to other enums directly using the `to-enum` operator. For further details, see DSL release notes: [DSL 9.42.0](https://github.com/finos/rune-dsl/releases/tag/9.42.0)
- `DSL` 9.43.0 Improved code-generation performance and patched the `with-meta` operator. For further details, see DSL release notes: [DSL 9.43.0](https://github.com/finos/rune-dsl/releases/tag/9.43.0)
- `DSL` 9.44.0 Added support for conditions on type aliases. For further details, see DSL release notes: [DSL 9.44.0](https://github.com/finos/rune-dsl/releases/tag/9.44.0)
- `DSL` 9.44.1 Patch for code-generating conditions. For further details, see DSL release notes: [DSL 9.44.1](https://github.com/finos/rune-dsl/releases/tag/9.44.1)
- `DSL` 9.45.1 Improved `to-meta` operator so it now ignores the Display Name when converting between two enumerations, see DSL release notes: [DSL 9.45.1](https://github.com/finos/rune-dsl/releases/tag/9.45.1)
  
_Review Directions_

The changes can be reviewed in PR [#3555](https://github.com/finos/common-domain-model/pull/3555) and [#3607](https://github.com/finos/common-domain-model/pull/3607).

