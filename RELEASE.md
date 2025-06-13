# _CDM Model - Addition of Product Grade Enumeration_

_Background_

In alignment with the CDM roadmap, the jurisdictional coverage of the model is being expanded and updated ensuring a robust support for evolving regulatory requirements. This release includes the modelling of the ProductGradeEnum.

_What is being released?_

The FpML coding scheme designated `productGradeScheme` (http://www.fpml.org/coding-scheme/commodity-oil-product-grade-1-0.xml) has been added in a new namespace named _cdm.base.staticdata.asset.commodity_.

The attribute intended to contain the type `ProductGradeEnum` is located within `AssetDeliveryInformation`, as it contains information relative to the delivery of the asset within `CommodityPayout`, and is named `commodityGrade`.

_Review directions_

Changes can be reviewed in PR: [#3799](https://github.com/finos/common-domain-model/pull/3799)
