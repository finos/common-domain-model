# *Collateral Taxonomy Sources - EU US & US Eligible Collateral Category Listings*

_What is being released?_

Collateral Taxonomy sources, Regulatory Enumeration lists to support the identification/labelling and categorisation of eligible collateral asset types per global regime regulations, as part of global uncleared derivatives margin rules for posting collateral margin. The lists added to the model are based on those published and supported under the following jurisdictional regulatory bodies:

•	European Union Eligible Collateral Assets classification categories based on EMIR Uncleared Margin Rules
•	United Kingdom Eligible Collateral Assets classification categories based on UK Onshored EMIR Uncleared Margin Rules
•	US Eligible Collateral Assets classification categories based on Prudential Regulators and CFTC Uncleared Margin Rules

Each of these enumerated values has a complete description of the relevant regulatory rules they represent. They are listed as  additional Taxonomy sources to those already represented in the CDM 


_Review directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:
•	In (base-staticdata-asset-common-emum) Search for `TaxonomySourceEnum`, added to this list are 3 new  enum values `EUEMIREligibleCollateralAssetClass`, `UKEMIREligibleCollateralAssetClass`,`USCFTCPREligibleCollateralAssetClass` these are followed by each enum list please observe the list under each respective regulation representation. `EUEMIREligibleCollateral` contains 18 types A to R, `UKEMIREligibleCollateral` contains 18 types A to R, `USCFTCPREligibleCollateral`contains 13 types. Each of the types has its own complete description using based on the respective regulations.
•	In (base-staticdata-asset-common-type) Search for `CollateralTaxonomy` which is a new type that sits under `ProductTaxonomy` The `CollateralTaxonomy` type allows you to specify a `TaxonomySource` and a value from the enumeration list. 
•	New conditions have been added for `EUEMIREligibleCollateral`,`UKEMIREligibleCollateral`, `USCFTCREligibleCollateral`That only allows you to select for the specific list you detail as your taxonomy source.
•	New type added `CollateralTaxonomyValue` which lists the available taxonomy value enumeration options with an additional option `nonEnumeratedTaxonomyValue` this allows the flexibility to specify and additional taxonomy value outside of the available enumerations. There is a condition added so you must choose one of the available collateral taxonomy values
•	In (legalagreement-csa-type) Within asset criteria – removed `ProductTaxonomy` and replaced with` CollateralTaxonomy`. Under related condition for optional choice – removed `ProductTaxonomy` Replaced with `CollateralTaxonomy`


