# Collateral Model - Collateral Guarantor added to Collateral Criteria

_Background_

There is a gap in the collateral model where a party cannot be specified to guarantee the collateral asset. Although users can specify the collateral issuer type, the guarantor is not supported in the collateral criteria choice type. A guarantor is a common requirement for collateral criteria. 

_What is being released?_

- `IssuerTypeEnum` is renamed to `CollateralEntityTypeEnum`. This enum can be reused as the entity type values are common across the issuer & guarantor. `IssuerTypeEnum` is used only in the `CollateralIssuerType` so there is minimal impact to other areas of the model. 
- `CollateralGuarantorType` is created which has the `CollateralEntityTypeEnum` as an attribute.
- `CollateralGuarantorType` is  added to `CollateralCriteria`.

_Review Directions_

Changes can be reviewed in PR: [#4258](https://github.com/finos/common-domain-model/pull/4258)
