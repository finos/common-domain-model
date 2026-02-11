# _Asset Model - Adding Redemption Attribute to Debt Type_
_Background_

There are several values in the the DebtClassEnum relating to the redemption of the debt which could be more granular and composable. There are 4 attributes representing unique combinations for IssuerConvertible, HolderConvertible, IssuerExchangeable, HolderExchangeable. However, these could be represented using separate enums and conditions within DebtType. This would also remove the additional Convertible attribute.

_What is being released?_

Created a DebtRedemption type - Added redemptionType attribute with type RedemptionTypeEnum - Added putCall attribute with type PutCallEnum - Added party attribute with type RedemptionPartyEnum Created 2 new enums - RedemptionTypeEnum with values Convertible, Exchangeable, ContingentConvertible, Sinkable, Extraordinary - RedemptionPartyEnum with values Holder and Issuer

_Review Directions_

The changes can be reviewed in PR: [#4447](https://github.com/finos/common-domain-model/pull/4447)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` , `bundle` and `FpML as Rune` dependency:

Version updates include:
- `DSL` `9.75.3` Performance improvements and bug fix. See DSL release notes: [9.75.3](https://github.com/finos/rune-dsl/releases/tag/9.75.3)
- `bundle` `11.108.0` Performance improvements and bug fix.
- `FpML as Rune` `1.5.0` See Release notes: [1.5.0](https://github.com/rosetta-models/rune-fpml/releases/tag/1.5.0).

_Review Directions_

The changes can be reviewed in PR: [#4411](https://github.com/finos/common-domain-model/pull/4411)

# *Product Model - EquityForward Qualification functions*

_Background_

There are no qualification functions for Equity Forwards.

_What is being released?_

Qualification Functions for Equity Forwards introduced:
- `Qualify_EquityForward_PriceReturnBasicPerformance_SingleName`
- `Qualify_EquityForward_PriceReturnBasicPerformance_SingleIndex`
- `Qualify_EquityForward_PriceReturnBasicPerformance_Basket`

_Review Directions_

The changes can be reviewed in PR: [#4405](https://github.com/finos/common-domain-model/pull/4405)
