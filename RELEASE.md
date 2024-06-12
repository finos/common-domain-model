# _Product Model - Fix for Portfolio Return Terms_

_Background_

The purpose of this update is to modify an existing condition relating to `Basket` which is causing unexpected behavior as well as a change in the cardinality required of `PerformancePayout` and `PortfolioReturnTerms`.

_What is being released?_

- Condition attached to `Basket` has been updated to `required choice basketConstituent, portfolioBasketConstituent` instead of previous condition: `one-of`.
- Cardinality of `PerformancePayout` attributes has been updated to `(0..*)` instead of `(0..1)` :
  - initialValuationPrice,
  - interimValuationPrice
  - finalValuationPrice
- Cardinality of `PortfolioReturnTerms` attributes has been updated to `(0..*)` instead of `(0..1)` :
  - quantity
  - initialValuationPrice
  - interimValuationPrice
  - finalValuationPrice

_Review Directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: [#2978](https://github.com/finos/common-domain-model/pull/2978)
