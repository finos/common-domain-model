# *Eligible Collateral Model* - Remove Eligible Collateral Instruction data type

_Background_

In CDM 6.0.0, the Eligible Collateral model was enhanced to enable the use of complex AND, OR and NOT logic in the combination of terms within a criteria.

Prior to this change, a function existed to clone an Eligible Collateral Specification.  Although this function was substantially refactored at that time, a data
type that it used (`EligibleCollateralSpecificationInstruction`) was left in place, albeit marked as deprecated.  This data type is now being removed.

_What is being released?_

The data type `EligibleCollateralSpecificationInstruction` has been removed from the model.

_Backward-incompatible changes_

This release is not backward compatible.

_Review Directions_

The change can be reviewed in PR: [#3515](https://github.com/finos/common-domain-model/pull/3515)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.34.2 bug fix where removing duplicate import causes problems where those imports use aliases. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.34.2
- `DSL` 9.35.0 bug fix for recursive reporting rules and support for labels on circular types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.35.0
- `DSL` 9.35.1 Rule source label fix and maintenance of generated serialization code. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.35.1
- `DSL` 9.36.0 Added condition support in typeAlias. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.0
- `DSL` 9.36.1 Use package name first segment for model name in RuneDataType annotation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.1
- `DSL` 9.36.2 Fix extended rule source with extended type. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.2
- `DSL` 9.36.3 Fix XML serializer substitution groups. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.3
- `DSL` 9.36.4 Make XML serializer substitution groups work in a backward compatible manner. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.4
- `DSL` 9.36.5 Handle empty inputs when set on meta function output & Fix for setting meta on nested objects. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.5
- `DSL` 9.36.6 Fix multi cardinality nested meta. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.6

_Review directions_

JSON expectations diffs are expected due to the added support for meta data in functions in DSL versions 9.36.6.

The changes can be reviewed in PR: [#3474](https://github.com/finos/common-domain-model/pull/3474) 
