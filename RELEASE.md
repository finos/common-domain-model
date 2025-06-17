# _CDM Model - Addition of Product Grade Enumeration_

_Background_

This release includes the extension of the `ProductGradeEnum` to expand the jurisdictional coverage for commodity products.

_What is being released?_

The FpML coding scheme designated `productGradeScheme` (http://www.fpml.org/coding-scheme/commodity-oil-product-grade-1-0.xml) has been added in a new namespace named _cdm.base.staticdata.asset.commodity_. The synonym mapping to the coding scheme has been also added to facilitate the ingestion of FpML messages.

The attribute intended to contain the type `ProductGradeEnum` is located within `AssetDeliveryInformation`, as it contains information relative to the delivery of the asset within `CommodityPayout`, and is named `commodityGrade`.

_Review directions_

Changes can be reviewed in PR: [#3799](https://github.com/finos/common-domain-model/pull/3799)

# _Collateral Model - Concentration Limit Update_

_Background_

A concentration limit is a type of collateral treatment defining concentration limits that may be applicable to eligible collateral criteria.

`ConcentrationLimitCriteria` is an attribute within `ConcentrationLimit` used to specify the criteria to which the limits apply. Within `ConcentrationLimitCriteria`, there is a condition which forces the user to select only one `ConcentrationLimitCriteria`.

However, because `ConcentrationLimitCriteria` extends `CollateralCriteriaBase` (where `collateralCriteria` is already a mandatory field), the condition always sets `collateralCriteria` as the `ConcentrationLimitTypeChoice`. Therefore, it is not possible to set `concentrationLimitType` or `averageTradingVolume`.

_What is being released?_

The update in this release allows users to set different types of concentration limits by relaxing cardinality on `collateralCritera` in `CollateralCriteriaBase` to (0..1). An `override` is added to the `collateralCriteria` attribute in `EligibleCollateralCriteria` to enforce the mandatory addition of a `collateralCriteria` to an eligible collateral schedule.

_Review Directions_

Changes can be reviewed in PR: [3701](https://github.com/finos/common-domain-model/pull/3701)

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
