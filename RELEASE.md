# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.34.2 bug fix where removing duplicate import causes problems where those imports use aliases. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.34.2
- `DSL` 9.35.0 bug fix for recursive reporting rules and support for labels on circular types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.35.0
- `DSL` 9.35.1 Rule source label fix and maintenance of generated serialization code. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.35.1
- `DSL` 9.36.0 Added condition support in typeAlias. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.0
- `DSL` 9.36.1 Use package name first segment for model name in RuneDataType annotation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.1
- `DSL` 9.36.2 Fix extended rule source with extended type. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.2
- `DSL` 9.36.3 Fix XML serializer substitution groups. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.3
- `DSL` 9.36.4 Make XML serializer substitution groups work in a backward compatible manner. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.4
- `DSL` 9.36.5 Handle empty inputs when set on meta function output & Fix for setting meta on nested objects. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.5
- `DSL` 9.36.6 Fix multi cardinality nested meta. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.6

_Review directions_

JSON expectations diffs are expected due to the added support for meta data in functions in DSL versions 9.36.6.

The changes can be reviewed in PR: [#3481](https://github.com/finos/common-domain-model/pull/3481) 