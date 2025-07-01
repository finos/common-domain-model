# _Infrastructure - Migrating CDM Artifacts publishing from `oss.sonatype.org` to `central.sonatype.com`_

_Background_

The Maven Central infrastructure is undergoing a major migration, from `oss.sonatype.org` to `central.sonatype.com`, which forces FINOS to make some changes to all project releases process ahead of June 30th.

Further information is provided by Sonatype:
https://central.sonatype.org/faq/what-is-different-between-central-portal-and-legacy-ossrh/#self-service-migration

_What is being released?_

Changes have been applied to release process to build and deploy to `central.sonatype.com` instead of `oss.sonatype.org`.

_Review Directions

No changes to model.

Changes can be review in PR: https://github.com/finos/common-domain-model/pull/3830
