# _Product Model - Qualifying functions: Enhanced support for ETDs_

_Background_

In CDM there is no support for Exchange-traded derivatives (ETDs) in some qualifying functions. This contribution enhances the support for ETD in `Qualify_AssetClass_InterestRate` and `Qualify_AssetClass_Commodity` when an optionUnderlier exists in the sample at "security" level.

_What is being released?_

- Added support for optionUnderlier when is ETD for `Qualify_AssetClass_InterestRate`.
- Added support for optionUnderlier when is ETD for Com Opt in `Qualify_AssetClass_Commodity`.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2733

# _Product Model - Day Count Fraction: RBA_Bond_Basis_

_Background_

The codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` in the CDM enum `DayCountFractionEnum` have been found redundant by definition. The solution to this issue is to merge them into one single code: `RBA_BOND_BASIS`. This also aligns with the FpML representation.

_What is being released?_

- Deprecated the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` and added the code `RBA_BOND_BASIS` in the CDM enum `DayCountFractionEnum`.
- Mapping added to populate the new code with the FpML code `RBA`.

_Review directions_

In Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

PR: https://github.com/finos/common-domain-model/pull/2726

# _Product Model - Commodity Forwards_

_Background_

This release provides qualification support for Commodity Forward products, which were not supported until now. The representation of commodity forwards pictures two possible kinds of forwards: fixed price forwards consisting in the combination of a `fixedPricePayout` and a `forwardPayout`; and floating price forwards consisting in the combination of a `commodityPayout` and a `forwardPayout`. The `forwardPayout` represents the physical delivery of the commodity while the other payout represents its pricing and the payment of a monetary amount in exchange.

_What is being released?_

- Added the qualifying function `Qualify_Commodity_Forward`.
- Updated the qualifying function `Qualify_AssetClass_Commodity` in order to consider commodity forwards.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2719

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-bundle` and `rosetta-dsl` dependencies.

Version updates include:
- `rosetta-bundle` 10.13.4: FpML Coding schema updated.
- `rosetta-dsl` 9.6.1: DSL bug fix for handing null values. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.6.1.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR: [#2708](https://github.com/finos/common-domain-model/pull/2708) / [#2729](https://github.com/finos/common-domain-model/pull/2729)
