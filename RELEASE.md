# _Product Model - Qualification of Foreign Exchange NDS_

_Background_

Currently, Foreign Exchange Non-Deliverable Swaps are not supported in the Common Domain Model. This release adds qualification support for this kind of product.

_What is being released?_

This release fixes the following functions to ensure an `else` clause is specified in all nested `if` statements.

- Added the function `Qualify_ForeignExchange_NDS` that qualifies as true if a product has two forward payouts with an FX underlier and the `cashSettlementTerms` populated.

_Review directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

The changes can be reviewed in  PR: [#2867](https://github.com/finos/common-domain-model/pull/2867)

# _Product Model - Qualification of Total Return Swaps (TRS) with a Debt Underlier_

_Background_

Following ESMA Guidelines, Total Return Swaps with a debt instrument as their underlier (bond, loan, etc) must report field 2.11 - `Asset Class` as 'CRDT', while TRS on an equity index or a basket of equities should report `Asset Class` as 'EQUI'. Currently in the CDM, a Total Return Swap with a debt underlier is not classified correctly, and thus is being reported incorrectly as well. This release aims at fixing the `Qualify_AssetClass_Credit` function such that Total Return Swaps on a bond or a loan report AssetClass as 'CRDT'.

_What is being released?_

- The function `Qualify_AssetClass_Credit` is increasing its coverage to include Total Return Swaps with an underlier of a `loan` or a `securityType` of `debt`.

_Functions_

- Updated `Qualify_AssetClass_Credit` function to support Total Return Swap products, defined as having an `interestRatePayout` and a `performancePayout`. The function checks the `performancePayout` that `underlier -> loan` is present or that `underlier -> security -> securityType = Debt`.

_Review directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

The changes can be reviewed in  PR: [#2856](https://github.com/finos/common-domain-model/pull/2856)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-bundle` and `rosetta-dsl` dependencies.

Version updates include:
- `rosetta-bundle` 10.16.0: FpML Coding schema updated.
- `rosetta-dsl` 9.8.0: this release features three new operations - `to-date`, `to-date-time` and `to-zoned-date-time` - to convert a string into a `date`, `dateTime` or `zonedDateTime` respectively. It also adds support to convert these three types into a string using the `to-string` operation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.8.0.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR: [#2876](https://github.com/finos/common-domain-model/pull/2876)
