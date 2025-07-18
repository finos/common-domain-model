# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.57.0 Measure and log build statistics, see DSL release notes: [DSL 9.57.0](https://github.com/finos/rune-dsl/releases/tag/9.57.0)
- `DSL` 9.58.0 Change detection fixes and performance improvements, see DSL release notes: [DSL 9.58.0](https://github.com/finos/rune-dsl/releases/tag/9.58.0)
- `DSL` 9.58.1 Patch Validator class for backward compatibility, see DSL release notes: [DSL 9.58.1](https://github.com/finos/rune-dsl/releases/tag/9.58.1)
- `DSL` 9.59.0 Continued migrating to Xtend and fixed null pointer issues in generated function code and type alias condition code, see DSL release notes: [DSL 9.59.0](https://github.com/finos/rune-dsl/releases/tag/9.59.0)
- `DSL` 9.60.0 Migration to Maven Central, see DSL release notes: [DSL 9.60.0](https://github.com/finos/rune-dsl/releases/tag/9.60.0)
- `DSL` 9.61.0 Fixed issues with type coercion and location setting, added support for using ^type, class, result, and package as attribute names, and improved the with-meta operation to work with empty arguments, see DSL release notes: [DSL 9.61.0](https://github.com/finos/rune-dsl/releases/tag/9.61.0)


There are no changes to model or test expectations.  The allocated memory configured to run the tests has been increased to prevent failures when running tests.

_Review Directions_

The changes can be reviewed in PR: [#3902](https://github.com/finos/common-domain-model/pull/3902)


# _CDM Distribution - Python Code Generation flag update_

_Background_

After an upgrade to using xtext, the incremental builds were running with value `true`. This resulted in the Python build not generating the expected number of enum files, as detailed in [Issue 3829](https://github.com/finos/common-domain-model/issues/3829).

_What is being released?_

Configuration changes have been made to set `incrementalXtextBuild` to `false` to ensure enum files are generated as expected.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3863

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
