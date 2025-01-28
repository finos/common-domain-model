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
