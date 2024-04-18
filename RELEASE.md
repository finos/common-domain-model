# _Product Model - Qualification of Total Return Swaps (TRS) with a Debt Underlier_

_Background_

Following ESMA Guidelines, Total Return Swaps with a debt instrument as their underlier (bond, loan, etc) must report field 2.11 - `Asset Class` as 'CRDT', while TRS on an equity index or a basket of equities should report `Asset Class` as 'EQUI'. Currently in the CDM, a Total Return Swap with a debt underlier is not classified correctly, and thus is being reported incorrectly as well. This release aims at fixing the `Qualify_AssetClass_Credit` function such that Total Return Swaps on a bond or a loan report AssetClass as 'CRDT'.

_What is being released?_

- The function `Qualify_AssetClass_Credit` is increasing its coverage to include Total Return Swaps with an underlier of a `loan` or a `securityType` of `debt`.

_Functions_

- Updated `Qualify_AssetClass_Credit` function to support Total Return Swap products, defined as having an `interestRatePayout` and a `performancePayout`. The function checks the `performancePayout` that `underlier -> loan` is present or that `underlier -> security -> securityType = Debt`.

_Review directions_

In Rosetta, select the Textual View and inspect the change identified above

The changes can be reviewed in PR: [#2855](https://github.com/finos/common-domain-model/pull/2855)

# _Product Model - Qualification of Foreign Exchange NDS_

_Background_

Currently, Foreign Exchange Non-Deliverable Swaps are not supported in the Common Domain Model. This release adds qualification support for this kind of product.

_What is being released?_

- Added the function `Qualify_ForeignExchange_NDS` that qualifies as true if a product has two forward payouts with an FX underlier and the `cashSettlementTerms` populated.
_Review directions_

In the Rosetta platform, select the Textual View and inspect each of the changes identified above.

PR: [#2866](https://github.com/finos/common-domain-model/pull/2866)

