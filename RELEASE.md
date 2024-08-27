# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency to `fix the Static compile issue` users are experiencing after upgrading workspaces. This release doesn't include any changes to model.

Version updates include:
- `DSL` 9.15.0: patch for supporting tabulation of types with circular dependencies. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.0
- `DSL` 9.15.1: patch for missing generated Java files. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.1
- `DSL` 9.15.2: patch for missing Java meta classes. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.2

_Review directions_

The changes can be reviewed in PR: [#3098](https://github.com/finos/common-domain-model/pull/3098)
