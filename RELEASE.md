# _Product Qualification - Amendment in filter conditions for Commodity Products_

_Background_

An issue was identified in the product qualification framework following updates introduced in [Issue #3476](https://github.com/finos/common-domain-model/issues/3476), whereby it was possible for products with non-standardised terms to be double-qualified. The patch that was added earlier missed one `Commodity Option` qualifier function. This patch addresses the issue. 

_What is being released?_

This release addresses this issue, amending the following functions that qualify non-exotic products to add a negative `nonStandardisedTerms` check:

Version updates include:
- `DSL` 9.38.0 Fixed setting enum values on meta fields. For further details, see DSL release notes: [DSL 9.38.0](https://github.com/finos/rune-dsl/releases/tag/9.38.0)
- `DSL` 9.39.0 Fixed default operation issue and added support for `with-meta` operation. For further details, see DSL release notes: [DSL 9.39.0](https://github.com/finos/rune-dsl/releases/tag/9.39.0)
- `DSL` 9.40.0 Added support for regulatory reference paths. For further details, see DSL release notes: [DSL 9.40.0](https://github.com/finos/rune-dsl/releases/tag/9.40.0)
- `DSL` 9.40.1 Deprecated `productType`, `eventType`, and `calculation`. For further details, see DSL release notes: [DSL 9.40.1](https://github.com/finos/rune-dsl/releases/tag/9.40.1)
- `DSL` 9.41.0 Enabled support for dynamic validation and injection to improve the configurability of the generated code. The change also included a fix that prevented correct operation of DSL quick actions. For further details, see DSL release notes: [DSL 9.41.0](https://github.com/finos/rune-dsl/releases/tag/9.41.0)
- `DSL` 9.41.1 Annotated POJO attributes to highlight address & locations. Fixed issue that was breaking translate parse handlers. For further details, see DSL release notes: [DSL 9.41.1](https://github.com/finos/rune-dsl/releases/tag/9.41.1)
- `DSL` 9.42.0 Added support for converting enums to other enums directly using the `to-enum` operator. For further details, see DSL release notes: [DSL 9.42.0](https://github.com/finos/rune-dsl/releases/tag/9.42.0)
- `DSL` 9.43.0 Improved code generation performance and patched the `with-meta` operator. For further details, see DSL release notes: [DSL 9.43.0](https://github.com/finos/rune-dsl/releases/tag/9.43.0)
- `DSL` 9.44.0 Added support for conditions on type aliases. For further details, see DSL release notes: [DSL 9.44.0](https://github.com/finos/rune-dsl/releases/tag/9.44.0)
- `DSL` 9.44.1 Patch for code generating conditions. For further details, see DSL release notes: [DSL 9.44.1](https://github.com/finos/rune-dsl/releases/tag/9.44.1)

_Review Directions_

The changes can be reviewed in PR [#3556](https://github.com/finos/common-domain-model/pull/3556) and [#3608](https://github.com/finos/common-domain-model/pull/3608).

* Qualify_Commodity_option

_Review Directions_

In the Rosetta platform, select the Textual Browser and inspect the change identified above, navigating to file cdm > product > qualification > func.

Details of the Issue and the resolution are available here: [#3579](https://github.com/finos/common-domain-model/issues/3579)

Changes can be reviewed in PR: [#3596](https://github.com/finos/common-domain-model/pull/3596)

# _Infrastructure - Dependency Update_

_Background_

The Rosetta platform has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

This release updates `ISOCurrencyCodeEnum` to keep it in sync with the latest ISO 4217 coding scheme.
- The following enum value have been added:
    - `XCG <"Caribbean Guilder">`

_Review directions_

In Rosetta, select the Textual Browser and inspect the change identified above.

Changes can be reviewed in PR: [#3596](https://github.com/finos/common-domain-model/pull/3596)
