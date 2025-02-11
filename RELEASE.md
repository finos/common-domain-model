# *CDM - A CDM user can access an extended Agreement model*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

The issue [#3348](https://github.com/finos/common-domain-model/issues/3348) proposes to migrate the ISDA Foundations project to the CDM, without the ISDA legal documentation IP

Preparation must be first be done in the CDM to synchronise it with the ISDA Foundations project and simplify the migration of any ISDA Foundations components.

_What is being released?_

As part of the preparation for the migration of any ISDA Foundations components to the CDM, this release creates an extended Agreement model in the CDM. This includes adding some (already sanitised) ISDA Foundations components into CDM, and moving other components around.

1. Added `BrokerConfirmationTypeEnum` in `legaldocumentation.contract.enum` 
2. Added `BrokerConfirmation` and `IssuerTradeId` to `legaldocumentation.contract.type`
3. Added `brokerConfirmationType` attribute to `AgreementName` 
4. Moved all empty types related to “additional terms” (used in `TransactionAdditionalTerms`) to a sub-namespace: `legaldocumentation.master.additionalterms`


_Backward-incompatible changes_

None.

_Review Directions_

The change can be reviewed in PR: [#3352](https://github.com/finos/common-domain-model/issues/3352).


# *CDM - A CDM user can access an extended FRO model*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

The issue [#3348](https://github.com/finos/common-domain-model/issues/3348) proposes to migrate the ISDA Foundations project to the CDM, without the ISDA legal documentation IP

Preparation must be first be done in the CDM to synchronise it with the ISDA Foundations project and simplify the migration of any ISDA Foundations components.

_What is being released?_

As part of the preparation for the migration of any ISDA Foundations components to the CDM, this release creates an extended FRO Model in the CDM. This includes adding some (already sanitised) ISDA Foundations components into CDM, and moving other components around.

1. Added extra attributes from `FloatingRateIndexDefinitionExtension` (and their required types) directly into `FloatingRateIndexDefinition`
2. Added extra attributes from `FloatingRateIndexCalculationDefaultsExtension` (and their required types) directly into `FloatingRateIndexCalculationDefaults`
3. Moved `ValidateFloatingRateIndexName` and `ValidateFloatingRateIndexTradeDate` to `observable.asset.fro`
4. Updated `ValidateFloatingRateIndexTradeDate` to call the existing `FloatingRateIndexMetadata` function iteratively.

_Backward-incompatible changes_

None.

_Review Directions_

The change can be reviewed in PR: [#3359](https://github.com/finos/common-domain-model/pull/3359).

# *CDM - A CDM user can access a contract's closed status component*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

The issue [#3348](https://github.com/finos/common-domain-model/issues/3348) proposes to migrate the ISDA Foundations project to the CDM, without the ISDA legal documentation IP

Preparation must be first be done in the CDM to synchronise it with the ISDA Foundations project and simplify the migration of any ISDA Foundations components.

_What is being released?_

As part of the preparation for the migration of any ISDA Foundations components to the CDM, this release allows CDM users to access a contract’s closed status component in the event namespace.

1. Moved `ClosedStateEnum` to `event.common.enum`
2. Moved `ClosedState` to `event.common.type`


_Backward-incompatible changes_

None.

_Review Directions_

The change can be reviewed in PR: [#3386](https://github.com/finos/common-domain-model/pull/3386).

# *CDM - Addition of changedCriteria to outputSpecification when using the CloneEligibleCollateralWithChangedTreatment function*

_Background_

The `CloneEligibleCollateralWithChangedTreatment` function creates a new Eligible Collateral Specification based on an input specification but with one changed criteria and one changed treatment.

Currently, when the function is invoked, the output of an `EligibleCollateralSpecification` only includes the `inputSpeciifcation` and the `changedTreatment`, but not the `changedCriteria`. This means that users can only change the treatment of a collateral item, but not a specific attribute.

_What is being released?_

This release sets an attribute of the `collateralCriteria` in the output to the `changedCriteria` specificied by the user when invoking the function.


_Backward-incompatible changes_

None.

_Review Directions_

This change can be reviewed in PR: [#3344](https://github.com/finos/common-domain-model/pull/3344)

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
