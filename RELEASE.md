# *Infrastructure - Security Update*

_What is being released?_

Third-party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

- `npm/axios` upgraded from version 0.30.1 to 1.12.0, see [GHSA-4hjh-wcwx-xvwj](https://github.com/advisories/GHSA-4hjh-wcwx-xvwj) for further details
- `npm/docusaurus` upgraded from version 2.4.1 to 3.8.1 upgraded from version 2.4.1 to 3.8.1 to remove a transitive dependency on axios 0.7.0.

_Review Directions_

Changes can be reviewed in PR: [#4054](https://github.com/finos/common-domain-model/pull/4054)

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

The changes can be reviewed in PR: [#4035](https://github.com/finos/common-domain-model/pull/4035)
