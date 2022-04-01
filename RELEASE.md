# *Legal Agreement Model - Rationalise Legal Agreement type identification*

_Background_

Legal agreements are not currently represented consistently in the CDM.  Legal agreements defined using the `DocumentationIdentification` data type are identified through a series of enumerations which point to individual instances of documents such as Master Agreements, Master Confirmation Agreements, Credit Support Agreements, Confirmations (e.g. Contractual Definitions, Contractual Terms Supplements etc) and Other Agreements.  Legal agreements defined used the `LegalAgreement` data type are identified through a composable set of attributes allowing the publisher, vintage, governing law and agremeent name to be specified.

The CDM Legal Agreement model is therefore being refactored to use a standardised approach that represents all legal agreements as composable data using the `LegalAgreement` data type.  Credit Support Agreements have been fully refactored to follow this approach.  Other documents will be re-factored in future releases.

_What is being released?_

This release contains the components required to represent Legal Agreements in line with the above approach. Model to model mappings have been updated to reflect this refactoring. Changes are described based on the data types, attributes, enumerations and functions impacted:

_Data Types_

`AgreementName` - new data type allowing specification of the agreement name through an agreement type and optional detailed sub agreement type.  In addition contains attributes previously found on `DocumentationIdentification`.

`DocumentationIdentification` has been removed.

`RelatedAgreement` has been removed.  All locations in the model that previously referenced this data type now reference `LegalAgreement`.

_Attributes_

`attachment` - attribute has been moved to `LegalAgreementBase` describing a human readable document containing a complete Legal Agreement.

`clauseLibrary` - boolean attribute has been moved to `AgreementTerms` defining whether agreement terms have been negotiated using the Clause Library methodology.

_Enumerations_

`LegalAgreementTypeEnum` - new enumeration containing values to identify the legal agreement type.  (e.g. Master Agreement, Confirmation, Credit Support Agreement.)

`CreditSupportAgreementTypeEnum` - enumerations have been adjusted to remove the document publisher, vintage and governing law from their names as this information can now be defined composably through the `LegalAgreementType`.

There has also been some minor changes to the label names and attributes of some of the related enumerations and data types.

In the CDM Portal, select the Textual Browser and search and inspect each of the changes identified above.

_Review Directions_

In the CDM Portal, select Ingestion and review the following samples with documentation:

Master Agreement, Master Confirmation and Confirmation
- fpml-5-10/products/credit/cdx-index-option.xml

Master Agreement and Confirmation
- fpml-5-10/products/credit/cd-ex01-long-asia-corp-fixreg-versioned.xml

Master Agreement and Credit Support Agreement
- events/novation-both-parties.xml
