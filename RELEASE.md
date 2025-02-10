# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the Rune dependencies.

Version updates include:
- DSL 9.34.1: Bug fix related to import organisation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.34.1
- DSL 9.34.0: Rune syntax to allow setting reference key/id meta data. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.34.0
- DSL 9.33.0: Rune syntax to allow setting reference meta data on nested data types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.33.0
- DSL 9.32.1: Bug fix for validation errors. Label annotation documentation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.32.1
- DSL 9.31.0: Rune syntax to allow setting reference meta data. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.31.0
- DSL 9.30.0: Label annotation support. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.30.0
- DSL 9.29.0: Bug fix for switch statements. Add support for import organisation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.29.0

This release also updates the FpML / ISO code scheme syncing configuration from exact matching to additive matching to ensure no backward incompatible changes, as per the [production version](https://cdm.finos.org/docs/contribution#version-availability) guidelines.

_Review Directions_

The changes can be reviewed in PR: [#3379](https://github.com/finos/common-domain-model/pull/3379)

# _Infrastructure - GitHub Actions_

_Background_

GitHub Actions is used to perform checks on Pull Requests (PRs) raised on [FINOS/common-domain-model](https://github.com/finos/common-domain-model).

_What is being released?_

This release fixes usage of GitHub Actions APIs that have been deprecated, as per the [documentation](https://github.blog/changelog/2024-04-16-deprecation-notice-v3-of-the-artifact-actions/).

_Review directions_

The changes can be reviewed in PR: [#3379](https://github.com/finos/common-domain-model/pull/3379)