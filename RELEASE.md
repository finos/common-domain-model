# _Infrastructure - Remove Unused Folders_

_What is being released?_

This release removes files and folders that were previously used by the CDM Portal.

Removed folders:
- distribution
- rosetta-source/src/main/resources/calculation-test-cases
- rosetta-source/src/main/resources/cdm-sample-files/event-sequences

There are no changes to the model.

_Review directions_

The changes can be reviewed in PR: [#2799](https://github.com/finos/common-domain-model/pull/2799)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-dsl` dependencies.

Version updates include:
- `rosetta-dsl` 9.7.0: DSL validation and performance enhancements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.7.0.

There are no changes to the model.  The number of expected ingestion validation failures has changed due to changes in the way validation failures are counted.

_Review directions_

The changes can be reviewed in PR: [#2780](https://github.com/finos/common-domain-model/pull/2780)
