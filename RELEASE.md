# *Product Model - Product Identification*

_Background_

This release refactors the data attributes and data types used to identify and classify products in the CDM.

_What is being released?_

Data type `ProductIdentification` has been removed from the CDM which previously contained a combination of concepts describing both the taxonomy of a product and also a `productIdentifier` used to identify a specific product.

The below attributes have been added to `ProductBase` which can now be used to consistently identify any type of Product in the model using two distinct concepts:

- `productIdentifier` - data type `ProductIdentifier` comprises an identifier and source to uniquely identify a product.  For example an ISIN assigned to a Security.
- `productTaxonomy` - data type `ProductTaxonomy` comprises attributes allowing the indication of a family, or class, that the product is a member of.  For example a CFI code, or the ISDA Taxonomy.

ProductTaxonomy is composed of the following attributes:
- `primaryAssetClass` - Classifies the most important risk class of the product
- `secondaryAssetClass` - Classifies additional risk classes of the trade, if any
- `taxonomyValue` - Specifies a taxonomy value assigned to the product, for example a CFI code, an ISDA Taxonomy value
- `taxonomySource` - Enumerates the taxonomy source associated with the `taxonomyValue` provided
- `productQualifier` - Derived from the product payout features using a CDM product qualification function that determines the product type based on the product payout features

When a `productQualifier` is derived for a product the associated `taxonomySource` value of `ISDA` is automatically populated as the CDM currently qualifies products using the ISDA Taxonomy classification scheme.

All data types defined as attributes of `Product` in the CDM (e.g. ContractualProduct, Security, Commodity) now extend `ProductBase` in order to inherit the attributes above.

_Review Directions_
 
In the CDM Portal, select Textual Browser and view the attributes and data types above.
In the CDM Portal, select Ingestion and view the following example trade to review the new model structure:
- fpml-5-10 > products > rates > USD-Vanilla-swap.xml

# *Product Model - Non-standard Terms*

_What is being released?_

A boolean attribute `nonStandardisedTerms` has been added to `EconomicTerms` in order to identify where economic details exist that have not been captured in the CDM representation of the product.  This has been done in order to develop support for the FpML `genericProduct` concept.

_Review Directions_
 
In the CDM Portal, select Textual Browser and view the attributes and data types above.

# *Product Model - PayoutQuantity cardinality*

_What is being released?_

The cardinality of `payoutQuantity` within data type `ProductBase` has been updated to be optional, this is to allow the model to correctly support Interest Rate Swaptions where the payoutQuantity is defined as part of the underlying product.  Interest Rate Swaption examples in the CDM were previously failing validation as there was no `payoutQuantity` value populated on the `optionPayout` component of the product representation.

Conditions have been added to all payout types other than `OptionPayout` to require a `payoutQuantity` to be populated.

_Review Directions_
 
In the CDM Portal, select Textual Browser and view the following data types:
- `PayoutBase` - note that `payoutQuantity` is now optional
- `InterestRatePayout` - note condition `Quantity` which requires a `payoutQuantity` to exist.  The same condition exists on all other Payouts apart from `OptionPayout`

In the CDM Portal, select Ingestion and view the following example, note that there is no longer a validation failure for a missing `payoutQuantity` on `optionPayout`:

- fpml-5-10 > products > rates > ird-ex09-euro-swaption-explicit-physical-exercise.xml
