# _Event Model - `empty` Value Handling Updates_

*Background*

An upcoming DSL release has found a number of areas where the use of `empty` in validation functions and conditions was not being handled correctly. This change contains fixes that prepare the model for the upcoming DSL release.

*What is being released?*

The following types have been updated:

- Instruction
    - Update condition `NewTrade` to return a valid status when `primitiveInstruction -> execution` is absent and `before` exists.

*Review Directions*

Changes can be reviewed in PR: [#4235](https://github.com/finos/common-domain-model/pull/4235)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` `9.68.1` Duplicate name detection. See DSL release notes: [DSL 9.68.1](https://github.com/finos/rune-dsl/releases/tag/9.68.1)
- `DSL` `9.69.0` Bug fix related to accessing enum values. See DSL release notes: [DSL 9.69.0](https://github.com/finos/rune-dsl/releases/tag/9.69.0)
- `DSL` `9.69.1` Fixed issue to do with overriding `ruleReference` annotations with `empty`. See DSL release notes: [DSL 9.69.1](https://github.com/finos/rune-dsl/releases/tag/9.69.1)
- `DSL` `9.70.0` Fixed validation null pointer. See DSL release notes: [DSL 9.70.0](https://github.com/finos/rune-dsl/releases/tag/9.70.0)

No expectations are updated as part of this release.

_Review Directions_

The changes can be reviewed in PR: [#4295](https://github.com/finos/common-domain-model/pull/4295)
