# *Legal Agreement Model - Collateral Taxonomy Sources: EU UK, & US Eligible Collateral Category Listings*

_What is being released?_

Collateral Taxonomy sources and Regulatory Enumeration lists to support the identification/labelling and categorisation of eligible collateral asset types per different regime's regulations, as part of global uncleared derivatives margin rules for posting collateral margin. The lists added to the model are based on those published and supported under the following jurisdictional regulatory bodies:

•	European Union Eligible Collateral Assets classification categories based on EMIR Uncleared Margin Rules
•	United Kingdom Eligible Collateral Assets classification categories based on UK Onshored EMIR Uncleared Margin Rules
•	US Eligible Collateral Assets classification categories based on Prudential Regulators and CFTC Uncleared Margin Rules

Each of the enumerated values has a complete description of the relevant regulatory rules they represent. They are listed as additional Taxonomy sources to those already represented in the CDM 


_Review directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:
•	Search for `TaxonomySourceEnum`, added to this list are 3 new  enum values `EUEMIREligibleCollateralAssetClass`, `UKEMIREligibleCollateralAssetClass`,`USCFTCPREligibleCollateralAssetClass` these are followed by each enum list please observe the list for each regime. `EUEMIREligibleCollateral` contains 18 types A to R, `UKEMIREligibleCollateral` contains 18 types A to R, `USCFTCPREligibleCollateral`contains 13 types. Each of the types has its own complete description based on the respective regulations.
•	Search for the new type `CollateralTaxonomy` under `ProductTaxonomy`. This new type specifies a `TaxonomySource` and a value from the enumeration list. 
•	New conditions have been added for `EUEMIREligibleCollateral`,`UKEMIREligibleCollateral`, `USCFTCREligibleCollateral`. These conditions enforces the specified regulatory enumerated list to match the taxonomy source.
•	A new type `CollateralTaxonomyValue` lists the available taxonomy value enumeration options as well as a `nonEnumeratedTaxonomyValue` that caters for taxonomy value outside of the available enumerated lists. A logical condition was added to ensure one collateral taxonomy values is specified.
•	`ProductTaxonomy` has been replaced with` CollateralTaxonomy` Within asset criteria and the related condition for optional choice.


