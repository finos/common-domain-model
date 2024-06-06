# *Eligible Collateral Representation - New Attributes*

_Background_

Through the Collateral Working Group, members have requested two enhancements to the modelling of collateral eligibility to enhance the CDM's capability to support
additional use cases:
1. To support the scenario where a legacy collateral schedule has shared criteria for IM and VM, with selected terms applicable to only one single margin type
2. To prioritise between Collateral Criteria where agency ratings are specified for both assets and issuers

_What is being released?_

- A new enumerator added to denote the different options available to identify how to prioritise the Agency Rating in a particular Issuer or Asset Criteria over
  the Agency Rating in another Criteria: `RatingPriorityResolutionEnum`.
- Two new attributes added to `CollateralCriteriaBase` to increase the specificity of the definition of the criteria in which collateral is eligible:
    - `restrictTo` to denote whether the criteria applies to only IM or VM, using the existing enumerator `CollateralMarginTypeEnum`
    - `ratingPriorityResolution` to denote whether the Issuer Criteria or Asset Criteria have precedence where there are multiple Agency Ratings defined,
  using the new `RatingPriorityResolutionEnum` enumerator

Both new attributes are optional, with singular cardinality, so this is a backward-compatible change. 

_Review Directions_

In Rosetta, open the contribution and view the changes listed above and inspect each of them.

Changes can be reviewed in PR [#2960](https://github.com/finos/common-domain-model/pull/2960)
