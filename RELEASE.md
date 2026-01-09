# *Asset - Redemption Attribute added to Debt type*

_Background_

There are several values in the `DebtClassEnum` relating to the redemption of debt which could be more granular and composable.

There are four attributes representing unique combinations for `IssuerConvertible`, `HolderConvertible`, `IssuerExchangeable`, `HolderExchangeable` whereas this could be better represented using separate enums and conditions within `DebtType`. This would also remove the additional Convertible attribute.

_What is being released?_

A new `DebtRedemption` type:
 - The attributes mentioned above can be combined to create combinations of Issuer/Convertible, Issuer/Call, Holder/Put etc.
 - Add `ConvertibleTypeEnum` as an attribute with `Convertible`, `Exchangeable`, `Sinkable` values
 - Add `putCallEnum` as an attribute
 - Add `RedemptionPartyElectionEnum` with values for `Issuer` and `Holder`

_Review Directions_

Changes can be reviewed in PR: [#4332](https://github.com/finos/common-domain-model/pull/4332)
