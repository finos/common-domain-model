# _CDM Distribution - Python Code Generation flag update_

_Background_

This fixes [Issues-3829](https://github.com/finos/common-domain-model/issues/3829) After upgrade to using xtext, the incremental builds were running with value `true`. This resulted in the Python build not generating same number enums as 5xx builds.

_What is being released?_

Configuration changes have been made to set `incrementalXtextBuild` to `false`

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
