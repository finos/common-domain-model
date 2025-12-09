# Collateral - Adding collateral guarantor to collateral criteria

_Background_

There is gap in the collateral model where you cannot specify the party guaranteeing the collateral asset.

_What is being released?_

Adding a `CollateralGuarantorType` to Collateral Criteria.

- Renamed `IssuerTypeEnum` to `CollateralEntityTypeEnum`. `IssuerTypeEnum` is used only in the `CollateralIssuerType` so there is minimal impact to other areas of the model
- Created a new type called `CollateralGuarantorType`
- Added this new type to `CollateralCriteria`

_Review Directions_

Changes can be reviewed in PR: [#4258](https://github.com/finos/common-domain-model/pull/4258)