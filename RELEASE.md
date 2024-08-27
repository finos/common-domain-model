# _Infrastructure - Dependency Update_

_Background_

`DSL` 9.14.1 had a bug which caused a null pointer when generating java files and missing Java meta classes in the `Rosetta` platform for the user's workspaces after upgrade their workspaces showing static compile errors due missing java files. 

_What is being released?_

This release updates the `DSL` dependency to `fix the static compile issue`. This release doesn't include any changes to model.

Version updates include:
- `DSL` 9.15.0: patch for supporting tabulation of types with circular dependencies. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.0
- `DSL` 9.15.1: patch for missing generated Java files. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.1
- `DSL` 9.15.2: patch for missing Java meta classes. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.2

_Review directions_

The changes can be reviewed in PR: [#3098](https://github.com/finos/common-domain-model/pull/3098)
