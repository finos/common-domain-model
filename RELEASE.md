# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.65.0 The `switch` operation now supports complex types. It also contains various model validation fixes. See DSL release notes: [DSL 9.65.0](https://github.com/finos/rune-dsl/releases/tag/9.65.0)
- `DSL` 9.65.1 Code generation fix for the `with-meta` operation. See DSL release notes: [DSL 9.65.1](https://github.com/finos/rune-dsl/releases/tag/9.65.1)
- `DSL` 9.65.2 Various fixes and build optimization. See DSL release notes: [DSL 9.65.2](https://github.com/finos/rune-dsl/releases/tag/9.65.2)
- `DSL` 9.65.3 Fixes related to `key` metadata. See DSL release notes: [DSL 9.65.3](https://github.com/finos/rune-dsl/releases/tag/9.65.3)
- `DSL` 9.65.4 Fix metadata template Java type. See DSL release notes: [DSL 9.65.4](https://github.com/finos/rune-dsl/releases/tag/9.65.4)
- `DSL` 9.65.5 Fix issue where clashing names from other namespaces are not correctly qualified in generated code. See DSL release notes: [DSL 9.65.5](https://github.com/finos/rune-dsl/releases/tag/9.65.5)

There are no changes to model or test expectations.

_Review Directions_

The changes can be reviewed in PR: [#4036](https://github.com/finos/common-domain-model/pull/4036)

# _Collateral Model - Updated descriptions for ConcentrationLimitTypeEnum_

_Background_

It has been raised that there is some ambiguity with the attribute descriptions under the `ConcentrationLimitTypeEnum`.
The word portfolio could mean several things; therefore, the CDM Collateral Working Group agreed to add additional language across the descriptions to ensure they indicate the collateral schedule being listed in the collateral criteria and avoid misinterpretation.

_What is being released?_

Updates to descriptions for `ConcentrationLimitTypeEnum` listings to remove 'portfolio' and replace this with 'eligible collateral schedule' where relevant and required.

_Review Directions_

The changes can be reviewed in PR: [#4027](https://github.com/finos/common-domain-model/pull/4027)
