# Collateral - Adding collateral guarantor to collateral criteria

_Background_

There is gap in the collateral model where you cannot specify the party guaranteeing the collateral asset. Although you can specify the collateral issuer type, the guarantor is not yet supported in the collateral criteria choice type. A guarantor is a common requirement for collateral criteria. 

_What is being released?_

Adding a `CollateralGuarantorType` to Collateral Criteria.

- Renamed `IssuerTypeEnum` to `CollateralEntityTypeEnum`. This enum can be reused as the entity type values are common across the issuer & guarantor. `IssuerTypeEnum` is used only in the `CollateralIssuerType` so there is minimal impact to other areas of the model. 
- Created a new type called `CollateralGuarantorType` which has the `CollateralEntityTypeEnum` as an attribute.
- Added `CollateralGuarantorType` to `CollateralCriteria`

_Review Directions_

Changes can be reviewed in PR: [#4258](https://github.com/finos/common-domain-model/pull/4258)
