# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` `9.71.0` Fixes incorrect treatment of empty for boolean resolution, equality, and implicit `else`. See DSL release notes: [DSL 9.71.0](https://github.com/finos/rune-dsl/releases/tag/9.71.0)

No expectations are updated as part of this release.

_Review Directions_

The changes can be reviewed in PR: [#4311](https://github.com/finos/common-domain-model/pull/4311)

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

This release updates the `DSL` dependency, and third-party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

Version updates include:
- `DSL` `9.68.1` Duplicate name detection. See DSL release notes: [DSL 9.68.1](https://github.com/finos/rune-dsl/releases/tag/9.68.1)
- `DSL` `9.69.0` Bug fix related to accessing enum values. See DSL release notes: [DSL 9.69.0](https://github.com/finos/rune-dsl/releases/tag/9.69.0)
- `DSL` `9.69.1` Fixed issue to do with overriding `ruleReference` annotations with `empty`. See DSL release notes: [DSL 9.69.1](https://github.com/finos/rune-dsl/releases/tag/9.69.1)
- `DSL` `9.70.0` Fixed validation null pointer. See DSL release notes: [DSL 9.70.0](https://github.com/finos/rune-dsl/releases/tag/9.70.0)

No expectations are updated as part of this release.

Third-party software library updates:
- `npm/axios` upgraded from version 0.30.1 to 1.12.0, see [GHSA-4hjh-wcwx-xvwj](https://github.com/advisories/GHSA-4hjh-wcwx-xvwj) for further details
- `npm/docusaurus` upgraded from version 2.4.1 to 3.8.1 to remove a transitive dependency on axios 0.7.0.

_Review Directions_

The changes can be reviewed in PR: [#4295](https://github.com/finos/common-domain-model/pull/4295)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency:

Version updates include:
- `DSL` `9.75.0` Suppress warnings annotation. See DSL release notes: [9.75.0](https://github.com/finos/rune-dsl/releases/tag/9.75.0)
- `DSL` `9.74.1` Fix usage of `default` with multi-cardinality. See DSL release notes: [9.74.1](https://github.com/finos/rune-dsl/releases/tag/9.74.1)
- `DSL` `9.74.0` Fix `empty` meta coercion. See DSL release notes: [9.74.0](https://github.com/finos/rune-dsl/releases/tag/9.74.0)
- `DSL` `9.73.0` Clean up DSL warnings. See DSL release notes: [9.73.0](https://github.com/finos/rune-dsl/releases/tag/9.73.0)
- `DSL` `9.72.0` Fix for serialisation. See DSL release notes: [9.72.0](https://github.com/finos/rune-dsl/releases/tag/9.72.0)

No changes to the test pack expectations.

_Review Directions_

The changes can be reviewed in PR: [#4316](https://github.com/finos/common-domain-model/pull/4316)