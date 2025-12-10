# Asset - Expanded Coverage for Secured Debt

_Background_

Different types of secured debt are not well represented in CDM.

In the current model, Asset Backed Securities could exist in CDM using the value in `DebtClassEnum`, however, there is no way to represent Mortgage Backed Securities or other types of secured debt.

_What is being released?_

This release supports the inclusion of secured debt by making the following changes:

- Created a new `SecuredDebt` type for different types of secured debt
- Added `secured` as an attribute under `DebtEconomics` with the `SecuredDebt` type
- Added `assetBacked`, `collateralizedObligations`, and `coveredBonds` as attributes in `SecuredType`, each with their own corresponding enumeration
- Added a `PropertyTypeEnum` to determine the type of property when the security is linked to a property asset
- Removed `AssetBacked` as a value from `DebtClassEnum`
- Added conditions to validate the values selected from `SecuredTypeEnum` correspond to the correct attribute in `SecuredType`
- Removed redundant "debt" prefix in the attributes of `DebtEconomics`

_Review Directions_

Changes can be reviewed in PR: [#4257](https://github.com/finos/common-domain-model/pull/4257)
