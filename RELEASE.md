# _Product Taxonomy Model - Adding "CSA" value in TaxonomySourceEnum_

_Background_

A model limitation has been identified in representing the taxonomy values of the underlying commodity asset defined by the CSA (Canadian Securities Administrators) Regulation. Identifying the taxonomy source as CSA is crucial for integrating the values into the model and populating the `CSA Commodity Underlyer ID` field modeled in DRR.

_What is being released?_

The proposal is to include the value `CSA` in the `TaxonomySourceEnum` to support the representation of the CSA Taxonomy values, needed to meet the reporting requirements for this regulation.

_Review Directions_

The changes can be reviewed in PR: [#3804](https://github.com/finos/common-domain-model/pull/3804)

# _Product Model - Additional values included within Product Id Type Enumeration_

_Background_

In alignment with the CDM roadmap, the jurisdictional coverage of the model is being expanded and updated, ensuring robust support for evolving regulatory requirements. This release includes the addition of two new values within `ProductIdTypeEnum`

_What is being released?_

This release includes the addition of `REDID` and `Valoren` values within `ProductIdTypeEnum`.

_Review Directions_

The changes can be reviewed in PR: [#3722](https://github.com/finos/common-domain-model/pull/3722)
