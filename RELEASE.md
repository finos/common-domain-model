# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.42.0 Added support for converting enums to other enums directly using the `to-enum` operator. For further details, see DSL release notes: [DSL 9.42.0](https://github.com/finos/rune-dsl/releases/tag/9.42.0)
- `DSL` 9.43.0 Improved code generation performance and patched the `with-meta` operator. For further details, see DSL release notes: [DSL 9.43.0](https://github.com/finos/rune-dsl/releases/tag/9.43.0)
- `DSL` 9.44.0 Added support for conditions on type aliases. For further details, see DSL release notes: [DSL 9.44.0](https://github.com/finos/rune-dsl/releases/tag/9.44.0)
- `DSL` 9.44.1 Patch for code generating conditions. For further details, see DSL release notes: [DSL 9.44.1](https://github.com/finos/rune-dsl/releases/tag/9.44.1)
- `DSL` 9.45.1 Ignore display name when converting between two enums, see DSL release notes: [DSL 9.45.1](https://github.com/finos/rune-dsl/releases/tag/9.45.1)

This release also updates the FpML / ISO code scheme syncing configuration to exact matching allowing backwards incompatible changes, as per the [development version guidelines](https://cdm.finos.org/docs/contributing/#version-availability).

_Review Directions_

The changes can be reviewed in PR: [#3597](https://github.com/finos/common-domain-model/pull/3597) 

