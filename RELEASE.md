# *Event Model - Transfer components: Rationalised representation of Quantity and Observable attributes*

_What is being released?_

This release adjusts the `Transfer` type by replacing the `PriceQuantity` attribute, with separate `Quantity` and optional `Observable` attributes.  In addition, a data constraint has been added to the `Transfer` type to validate that when an `Observable` is specified that the `Quantity->unitOfAmount->financialUnit` is also specified.

_Review directions_

In the CDM Portal, select the Ingestion panel, and review in the following samples the details of the transfer history associated to the after state of the transfer primitive that composes the business event.

- events > exercise-swaption-cash
- events > increase-xccy
- events > partial-termination-xccy

# *Legal Agreement Model - Collateral Taxonomy Sources: EU UK, & US Eligible Collateral Category Listings*

_What is being released?_

Collateral Taxonomy sources and Regulatory Enumeration lists to support the identification/labelling and categorisation of eligible collateral asset types per different regime's regulations, as part of global uncleared derivatives margin rules for posting collateral margin. The lists added to the model are based on those published and supported under the following jurisdictional regulatory bodies:

- European Union Eligible Collateral Assets classification categories based on EMIR Uncleared Margin Rules
- United Kingdom Eligible Collateral Assets classification categories based on UK Onshored EMIR Uncleared Margin Rules
- US Eligible Collateral Assets classification categories based on Prudential Regulators and CFTC Uncleared Margin Rules

Each of the enumerated values has a complete description of the relevant regulatory rules they represent. They are listed as additional Taxonomy sources to those already represented in the CDM

_Review directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:

- Search for `TaxonomySourceEnum` and inspect the new  enum values `EU_EMIR_EligibleCollateralAssetClass`, `UK_EMIR_EligibleCollateralAssetClass`,`US_CFTC_PR_EligibleCollateralAssetClass`. Each corresponds to a regulatory enumeration list for a regime: `EU_EMIR_EligibleCollateral` contains 18 enum values A to R, `UK_EMIR_EligibleCollateral` contains 18 enum values A to R, `US_CFTC_PR_EligibleCollateral`contains 13 enum values. Each of the enum values has its own complete description based on the corresponding regulations.
- Search for the new type `CollateralTaxonomy` under `ProductTaxonomy`. This new type specifies a `TaxonomySource` and a value from the enumeration list.
- New conditions have been added for `EU_EMIR_EligibleCollateral`,`UK_EMIR_EligibleCollateral`, `US_CFTC_PR_EligibleCollateral`. These conditions enforces the specified regulatory enumerated list to match the taxonomy source.
- A new type `CollateralTaxonomyValue` lists the available taxonomy value enumeration options as well as a `nonEnumeratedTaxonomyValue` that caters for taxonomy value outside of the available enumerated lists. A logical condition was added to ensure one collateral taxonomy values is specified.
- `ProductTaxonomy` has been replaced with` CollateralTaxonomy` Within asset criteria and the related condition for optional choice.



