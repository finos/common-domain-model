# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the DSL dependency, and third-party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

Version updates include:
- `DSL` `9.75.0` Suppress warnings annotation. See DSL release notes: [9.75.0](https://github.com/finos/rune-dsl/releases/tag/9.75.0)
- `DSL` `9.74.1` Fix usage of `default` with multi-cardinality. See DSL release notes: [9.74.1](https://github.com/finos/rune-dsl/releases/tag/9.74.1)
- `DSL` `9.74.0` Fix `empty` meta coercion. See DSL release notes: [9.74.0](https://github.com/finos/rune-dsl/releases/tag/9.74.0)
- `DSL` `9.73.0` Clean up DSL warnings. See DSL release notes: [9.73.0](https://github.com/finos/rune-dsl/releases/tag/9.73.0)
- `DSL` `9.72.0` Fix for serialisation. See DSL release notes: [9.72.0](https://github.com/finos/rune-dsl/releases/tag/9.72.0)

No expectations are updated as part of this release.

Third-party software library updates:

- `npm/qs` upgraded from version 6.13.0 to 6.14.1, see [GHSA-6rw7-vpxm-498p](https://github.com/advisories/GHSA-6rw7-vpxm-498p) for further details

_Review Directions_

The changes can be reviewed in PR: [#4314](https://github.com/finos/common-domain-model/pull/4314)