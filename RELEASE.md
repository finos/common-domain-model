# _Product Model - Portfolio Return Terms_

_Background_

Purpose is to release a Dev version for Portfolio Return Terms, in which the components with **[deprecated]** annotation in current Prod version have been removed.
As an indication, this release also contains a renaming of existing attributes in ValuationDates.

_What is being released?_

- removed [deprecated] attributes below from type **PriceReturnTerms** :
  - **valuationPriceInitial**
  - and **valuationPriceFinal**
  - and **finalValuationPrice**
- added attributes below to type **PerformancePayout** : that is exact release to Dev of the same already in Prod today, so kindly review descriptions in corresponding release notes
  - **portfolioReturnTerms** of type **PortfolioReturnTerms**
- updated the name of **ValuationDates** attribute below :
  - **initialValuationDate** (instead of prior name valuationDatesInitial)
  - **interimValuationDate** (instead of prior name valuationDatesInterim)
  - **finalValuationDate** (instead of prior name valuationDatesFinal)

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in PR: [#2974](https://github.com/finos/common-domain-model/pull/2974)
