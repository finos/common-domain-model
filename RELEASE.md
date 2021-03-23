# Technical Change - Builder Refactor*

_What is being released?_

Collateral Taxonomy Enum lists to support the identification of eligible collateral asset classes (collateral types) identified and categorised under global regime regulations. The enum lists added to the model in this first tranche are those published and supported under the following jurisdictional regulatory bodies as part of global uncleared derivatives margin rules for posting initial and variation margin.

European Union Eligible Collateral Assets classification categories based on EMIR Uncleared Margin Rules
United Kingdom Eligible Collateral Assets classification categories based on UK Onshored EMIR Uncleared Margin Rules
US Eligible Collateral Assets classification categories based on Prudential Regulator, CFTC and SEC Uncleared Margin Rules

Each of these enumerated values has a complete description of the relevant regulatory rules they represent.

_How to review the change_

Under `base-staticdata-asset-common-enum`, go to `enum TaxonomySourceEnum`the new enumerations are listed here, names as follows `EuEligibleCollateral``UkEligibleCollateral``UsEligibleCollateral` these are followed by each enum list please observe the list under each respective regulation representation. `EuEligibleCollateral` contains 18 types A to R, `UkEligibleCollateral` contains 18 types A to R, `UsEligibleCollateral` contains 13 types. Each of the types has its own complete description using text provided from the respective regulations.

Under `base-staticdata-asset-common-type`, go to `type ProductTaxonomy` this has been removed and replaced with `type CollateralTaxonomy` which has conditions added for `EuEligibleCollateral``UkEligibleCollateral``UsEligibleCollateral` and an additional `type CollateralTaxonomyValue`.

Under `legalagreement-csa-type`, go to `productTaxonomy`, this has been removed and replaced with `collateralTaxonomy` with supporting descriptions and related optional choice.








