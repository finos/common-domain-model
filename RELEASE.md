# _Product Model - Qualification of Foreign Exchange NDS_

_Background_

Currently, Foreign Exchange Non-Deliverable Swaps are not supported in the Common Domain Model. This release adds qualification support for this kind of product.

_What is being released?_

- Added the function `Qualify_ForeignExchange_NDS` that qualifies as true if a product has two forward payouts with an FX underlier and the `cashSettlementTerms` populated.
_Review directions_

In the Rosetta platform, select the Textual View and inspect each of the changes identified above.

PR: [#2866](https://github.com/finos/common-domain-model/pull/2866)

