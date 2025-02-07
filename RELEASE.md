# _Infrastructure - Dependency Update_

_Background_

Rune DSL has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated. 

_What is being released?_

This release updates the dependencies to version `11.39.0`

Version updates include:
- DSL 9.34.1: Temporarily disable removal of unused imports. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.34.1
- DSL 9.34.0: Set metadata key  For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.34.0
- DSL 9.33.0: Setting meta in nested objects For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.33.0
- DSL 9.32.1: Fixed validation propagated errors, Label annotation documentation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.32.1
- DSL 9.31.0 setting meta. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.31.0
- DSL 9.30.0 Label annotation support. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.30.0
- DSL 9.29.0: Loosen switch statements, Add support for import organization. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.29.0
  
This release updates the syncing feature to AdditiveMatch, ensuring no backward compatibility issues.

The changes can be reviewed in PR: [#3379](https://github.com/finos/common-domain-model/pull/3379)
