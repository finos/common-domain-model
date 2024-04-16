# _Product Model - Qualification of AssetClass_

_Background_

Issue [#2863](https://github.com/finos/common-domain-model/issues/2863) was identified with the recent change to the qualification of `AssetClass` in PR [#2840](https://github.com/finos/common-domain-model/pull/2840).

_What is being released?_

This release fixes the following functions to ensure an `else` clause is specified in all nested `if` statements.

- `Qualify_AssetClass_InterestRate`
- `Qualify_AssetClass_Credit` 
- `Qualify_AssetClass_ForeignExchange` 
- `Qualify_AssetClass_Equity` 
- `Qualify_AssetClass_Commodity`

_Review directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

The changes can be reviewed in  PR: [#2864](https://github.com/finos/common-domain-model/pull/2864)
