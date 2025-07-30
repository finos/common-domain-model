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
- `DSL` 9.62.0 Java code generation fixes related to setting metadata. See DSL release notes: [DSL 9.62.0](https://github.com/finos/rune-dsl/releases/tag/9.62.0)
- `DSL` 9.63.0 Changes the generated Java pruning algorithm to keep required attributes, even if they are empty. See DSL release notes: [DSL 9.63.0](https://github.com/finos/rune-dsl/releases/tag/9.63.0)

Test expectations have been updated accordingly to include required empty model objects that were previously pruned.

_Review Directions_

The changes can be reviewed in PR: [#3944](https://github.com/finos/common-domain-model/pull/3944)
