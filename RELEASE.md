# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the rune dependencies.

Version updates include:
- DSL 9.27.0: addresses a bug where the `switch` operator could sometimes break the model. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.27.0

_Review Directions_

The changes can be reviewed in PR: [#3315](https://github.com/finos/common-domain-model/pull/3315).



# *CDM Model - CollateralCriteria Asset and Index fields*

_Background_

CollateralCriteria was refactored, with Asset and Index choice data types temporarily blocked/commented until the DSL fix for switch statements was completed. More details are above under the version updates, with further details in the DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.27.0


_What is being released?_

This release reverted the commented out Asset and Index fields in CollateralCriteria after the bug was fixed.

_Backward-incompatible changes_

None.

_Review Directions_

The change can be reviewed in PR: [#3321](https://github.com/finos/common-domain-model/pull/3321).
