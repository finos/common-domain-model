# _Product Model - Portfolio Return Terms_

_Background_

Purpose is to release a Dev version for Portfolio Return Terms, in which the components with [**deprecated**] annotation in current Prod version have been removed.
As an indication, this release also contains a renaming of existing attributes in **ValuationDates**.

_What is being released?_

- removed [deprecated] attributes below from type **PriceReturnTerms** :
  - **valuationPriceInitial** 
  - and **valuationPriceFinal** 
  - and **finalValuationPrice**
- added attributes below to type **PerformancePayout** : that is core release to Dev of the same components previously released in Prod today :
  - creation of new type **PorfolioReturnTerms** which extends **ReturnTerms**
    - with existing types **PayerReceiver**
    - also with existing type **PriceSchedule** to **PerformancePayout** used under three attribute names, and **NonNegativeQuantitySchedule** used for one, respectively : **initialValuationPrice**, **interimValuationPrice**, **finalValuationPrice** and **quantity**
  - added the above new type to **PerformancePayout**
  - added existing type **PriceSchedule** to **PerformancePayout** used under three attribute names : **initialValuationPrice**, **interimValuationPrice** and **finalValuationPrice** (that is to replace [**deprecated**] ones removed from **ReturnTerms**)
  - updated type Basket with below changes :
    - removed temporary **PorfolioBasketConsituent** of type **BasketConstituent**
    - removed as well [deprecated] **basketConstituent** of type **Product**
    - added instead new attribute **basketConstituent** of type **BasketConstituent**
  - renamed attributes below in **ValuationDates** :
    - **initialValuationDate** (instead of **valuationDatesInitial**)
    - **interimValuationDate** (instead of **valuationDatesInterim**)
    - **finalValuationDate** (instead of **valuationDatesFinal**)

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect the change identified above.

_Backward compatibility_

As an information, this release contains two sets of changes with no backward compabiltity :

- removing [deprecated] components above mentioned
- renaming of attribtues in ValuationDates above mentioned

The changes can be reviewed in PR: [#2974](https://github.com/finos/common-domain-model/pull/2974)
