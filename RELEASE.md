# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.9.0: this release features a new operator - `default` - which takes in two expressions, and gives back its left result if it is present, otherwise gives back its right result. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.9.0.
- `DSL` 9.10.0: this release adds syntax for `choice` types - a shorthand for defining a data type with a `one-of` condition. It also introduces the "deep path operator" `->>`, which allows you to directly access common attributes nested inside a choice type. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.10.0.

_Review Directions_

The changes can be reviewed in PR: [#2969](https://github.com/finos/common-domain-model/pull/2969)
