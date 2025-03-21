# *Eligible Collateral Model* - Remove Eligible Collateral Instruction data type

_Background_

In CDM 6.0.0-dev.90, the Eligible Collateral model was enhanced to enable the use of complex AND, OR and NOT logic in the combination of terms within a criteria.

Prior to this change, a function existed to clone an Eligible Collateral Specification.  Although this function was substantially refactored at that time, a data
type that it used (`EligibleCollateralSpecificationInstruction`) was left untouched.  This is now being corrected.

_What is being released?_

The following changes have been made:

- The data type `EligibleCollateralSpecificationInstruction` has been annotated as deprecated (and will be deleted in CDM 7).
- The annotation `[RootType]` has been removed from this data type.  This annotation supported its use in the CDM Object Builder which is no longer required.

_Backward-incompatible changes_

This release is backwardly compatible.  A breaking change, to remove the data type, will be included in CDM 7.0.

_Review Directions_

The change can be reviewed in PR: [#3513](https://github.com/finos/common-domain-model/pull/3513)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.38.0 Fix setting enum values on meta fields. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.38.0
- `DSL` 9.39.0 Fix default operation issue and add support for `with-meta` operation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.39.0
- `DSL` 9.40.0 Add support for regulatory reference paths. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.40.0
- `DSL` 9.40.1 Deprecated productType, eventType and calculation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.40.1
- `DSL` 9.41.0 Fixed Simple NPE when code action is not set correctly. Deleted 'new' validator. Type format and cardinality validators injection. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.41.0
- `DSL` 9.41.1 Annotate POJO attributes to highlight address & locations. Fix issue that is breaking translate parse handlers. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.41.1

_Review directions_

The changes can be reviewed in PR: [#3555](https://github.com/finos/common-domain-model/pull/3555) 