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
