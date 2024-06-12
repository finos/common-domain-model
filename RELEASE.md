# _Product Model - Fix for Portfolio Return Terms_

_Background_

The purpose is to correct one blocking error related to condition, and minor fix about cardinality of some components, embedded with past release of Portfolio Return Terms.

_What is being released?_

- Condition attached to `Basket` has been updated to `required choice basketConstituent, portfolioBasketConstituent` instead of previous condition: `one-of`.
- Cardinality of `PerformancePayout` attributes has been updated to `(0..*)` instead of `(0..1)` :
  - initialValuationPrice,
  - interimValuationPrice
  - finalValuationPrice
- Cardinality of `PortfolioReturnTerms` attribute has been updated to `(0..*)` instead of `(0..1)` :
  - quantity
  - initialValuationPrice
  - interimValuationPrice
  - finalValuationPrice

_Review Directions_

In the Rosetta platform, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in PR: [#2978](https://github.com/finos/common-domain-model/pull/2978)
