# *Legal Agreement Model - Rationalise Legal Agreement type identification*

_Background_

The representation of legal agreements lacks consistency.  Some agreements use the `DocumentationIdentification` data type and are identified through a series of enumerations that point to individual instances of documents such as Master Agreements, Master Confirmation Agreements, Credit Support Agreements, Confirmations (e.g. Contractual Definitions, Contractual Terms Supplements etc) and Other Agreements.  Some agreements use the `LegalAgreement` data type and are identified through a composable set of attributes allowing the publisher, vintage, governing law and agremeent name to be specified.

The representation of Legal Agreement  is therefore  refactored through a standardised composition of data types using the `LegalAgreement` data type as anchor.  Credit Support Agreements have been fully refactored to follow this approach.  Other documents will be re-factored in future releases.

_What is being released?_

This release contains the components required to represent Legal Agreements per the above approach. Model to model mappings have been updated. Changes are described based on the data types, attributes, enumerations and functions impacted.

_Data Types_

`AgreementName` - This new data type  categorises the agreement, describes addtional features and provides for the attributes previously found in `DocumentationIdentification`.

`DocumentationIdentification` - This has been removed.

`RelatedAgreement` - This has been removed.  All references have been updated to point to `LegalAgreement`.

_Attributes_

`attachment` - This attribute has been moved to `LegalAgreementBase` to point to supplementary external documents.

`clauseLibrary` - This boolean attribute has been moved to `AgreementTerms` defining whether agreement terms have been negotiated using the Clause Library methodology.

_Enumerations_

`LegalAgreementTypeEnum` - This new enumeration identifies the legal agreement type (e.g. Master Agreement, Confirmation, Credit Support Agreement).

`CreditSupportAgreementTypeEnum` - This enumeration has been adjusted to remove the document publisher, vintage and governing law from their names as this information is now supported with the `LegalAgreementType` component

This change also adjusts the label names and attributes of some of the related enumerations and data types.

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples with documentation:

Master Agreement, Master Confirmation and Confirmation
- fpml-5-10/products/credit/cdx-index-option.xml

Master Agreement and Confirmation
- fpml-5-10/products/credit/cd-ex01-long-asia-corp-fixreg-versioned.xml

Master Agreement and Credit Support Agreement
- events/novation-both-parties.xml
