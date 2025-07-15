# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.57.0 Measure and log build statistics, see DSL release notes: [DSL 9.57.0](https://github.com/finos/rune-dsl/releases/tag/9.57.0)
- `DSL` 9.58.0 Change detection fixes and performance improvements, see DSL release notes: [DSL 9.58.0](https://github.com/finos/rune-dsl/releases/tag/9.58.0)
- `DSL` 9.58.1 Patch Validator class for backwards compatibility, see DSL release notes: [DSL 9.58.1](https://github.com/finos/rune-dsl/releases/tag/9.58.1)

There are no changes to model or test expectations.  The allocated memory configured to run the tests has been increased to prevent failures when running tests.

_Review Directions_

The changes can be reviewed in PR: [#3900](https://github.com/finos/common-domain-model/pull/3902)


# _Infrastructure - Release Process updates following Maven Central Migration_

_Background_

The Maven Central infrastructure is undergoing a major migration, from `oss.sonatype.org` to `central.sonatype.com`. This impacts the release process for all projects hosting artifacts using Maven Central.

Further information is provided by Sonatype:
https://central.sonatype.org/faq/what-is-different-between-central-portal-and-legacy-ossrh/#self-service-migration

_What is being released?_

The release process has been updated to build and deploy to `central.sonatype.com` instead of `oss.sonatype.org`.

_Review Directions_

No changes made to model.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3832
