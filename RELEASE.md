# _Asset Model - Adding Redemption Attribute to Debt Type_
_Background_

There are several values in the the `DebtClassEnum` relating to the redemption of the debt which could be more granular and composable. There are 4 attributes representing unique combinations for `IssuerConvertible`, `HolderConvertible`, `IssuerExchangeable`, `HolderExchangeable`. However, these could be represented using separate enums and conditions within DebtType. This would also remove the additional Convertible attribute.

_What is being released?_

Created a DebtRedemption type 
- Added `redemptionType` attribute with type `RedemptionTypeEnum` 
- Added `putCall` attribute with type `PutCallEnum` 
- Added `party` attribute with type `RedemptionPartyEnum` 

Created 2 new enums 
- `RedemptionTypeEnum` with values Convertible, Exchangeable, ContingentConvertible, Sinkable, Extraordinary
- `RedemptionPartyEnum` with values Holder and Issuer

_Review Directions_

The changes can be reviewed in PR: [#4447](https://github.com/finos/common-domain-model/pull/4447)
