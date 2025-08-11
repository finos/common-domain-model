# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.62.0 Java code generation fixes related to setting metadata. See DSL release notes: [DSL 9.62.0](https://github.com/finos/rune-dsl/releases/tag/9.62.0)
- `DSL` 9.63.0 Changes the generated Java pruning algorithm to keep required attributes, even if they are empty. See DSL release notes: [DSL 9.63.0](https://github.com/finos/rune-dsl/releases/tag/9.63.0)
- `DSL` 9.64.0 See DSL release notes: [DSL 9.64.0](https://github.com/finos/rune-dsl/releases/tag/9.64.0)
  * Show error for duplicate attributes in the same type
  * Add support for XML union elements
  * Added workaround for XSD `any` element
  * Reverted to prune required fields, added config to disable pruning
  
Test expectations have been updated accordingly to include required empty model objects that were previously pruned.

_Review Directions_

The changes can be reviewed in PR: [#3944](https://github.com/finos/common-domain-model/pull/3944)
