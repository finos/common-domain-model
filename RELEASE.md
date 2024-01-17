# *Eligible Collateral Schedule Model - CheckEligibilityResult cardinality fix*

_Background_

The `CheckEligibilityResult` data type holds the data returned by the 
`CheckEligbilityByDetails` function which is used to see whether certain
assets or issuers are eligible to be posted as collateral for a given
collateral eligibility schedule.

_What is being released?_

This release updates the `CheckEligibilityResult` data type.  Specifically,
the cardinality on two attributes has been corrected such that:
- `matchingEligibleCriteria` can be empty if there is no match (i.e. the
collateral is not eligible)
- `eligibilityQuery` must be present as this is a copy of the query input
provided to the function.

_Backward-Incompatible Changes_

None

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the
change identified above.  The changes can be reviewed in PR https://github.com/finos/common-domain-model/pull/2629
