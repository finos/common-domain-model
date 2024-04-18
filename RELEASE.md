# _Product Model - Qualification of Foreign Exchange NDS_

_Background_

Currently, Foreign Exchange Non-Deliverable Swaps are not supported in the Common Domain Model. This release adds qualification support for this kind of product.

_What is being released?_

This release fixes the following functions to ensure an `else` clause is specified in all nested `if` statements.

- Added the function `Qualify_ForeignExchange_NDS` that qualifies as true if a product has two forward payouts with an FX underlier and the `cashSettlementTerms` populated.

_Review directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

The changes can be reviewed in  PR: [#2867](https://github.com/finos/common-domain-model/pull/2867)
