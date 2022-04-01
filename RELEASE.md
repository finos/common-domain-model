# *Legal Agreement Model - Rationalise Legal Agreements*

_Background_

Legal agreements are not currently represented consistently in the CDM.  For example Master Agreements, Master Confirmation Agreements, Credit Support Agreements, Confirmations (e.g. Contractual Definitions, Contractual Terms Supplements etc) and Other Agreements are all modelled as distinct data types, but all contain similar data.

The CDM Legal Agreement model is therefore being refactored to use a standardised approach that represents all legal agreements as `LegalAgreement` data type.

_What is being released?_



`DocumentIdentification` data type is being removed and attributes are now found in the LegalAgreementIdentification data type (`LegalAgreementBase` >`agreementType` > `LegalAgreementIdentification`), which replaces `LegalAgreementType`

`CreditSupportAgreement` enumerations have been adjusted to remove the document publisher, vintage and governing law from their names as this information is now in the `LegalAgreementIdentification` type.

`RelatedAgreement `has been adjusted to point directly to legal agreement.

There has also been some minor changes to the label names and attributes of some of the related enumerations and data types.

Other areas of the CDM model that are impacted by these changes such as synonyms and conditions have been updated also as necessary.

In the CDM Portal, select the Textual Browser and search and inspect each of the changes to the below data types, their attributes and descriptions. They will be found in `legalagreement-common-type’ name space:
1. `ContractualMatrix` removed attribute `publicationDate`
2. `ContractualTermSupplement` removed
3. `RelatedAgreement` removed
4. `DocumentationIdentification` removed, all its attributes and related conditions
5. `LegalAgreement` attribute relatedAgreements amended to point direct to data type `LegalAgreement`
6. `LegalAgreement` data type related conditions have been updated with all required changes relevant to this contribution
7. `LegalAgreementType` renamed to data type ‘LegalAgreementIndentification’
8. ` LegalAgreementIndentification` attribute `name` removed
9. `LegalAgreementIndentification` new attribute added `agreementname`

10. New data type added `AgreementName` along with the following attributes:  
       •	`masterAgreementType`
       •	`creditSupportAgreement`
       •	`securityAgreement`
       •	`clauseLibrary`
       •	`masterConfirmationType`
       •	`masterConfirmationAnnexType`
       •	`brokerConfirmationType`
       •	`contractualDefinitionsType`
       •	`contractualTermsSupplementType`
       •	`contractualMatrix`
       •	`otherAgreement`
       •	`attachment`
11. In data type `AgreementName` addition of synonym (AcadiaSoft- DocumentNameEnum) moved from `legalAgreementNameEnum`
12. A condition `AgreementTypeChoice` to only determine one agreement type is added

In namespace event-common-type inspect each of the changes as follows:
1.	Review all changes to related conditions where `documentationIdentification` is replaced with `agreementType` > `agreementName`
2.	Data type `ContractDetails` attribute `documentation` has been updated to point to `LegalAgreement` instead of `RelatedAgreement`
3.	Data type `MarginCallBase` attribute `callAgreementType` has been updated to point to `LegalAgreementIdentification` instead of `LegalAgreementType`

In namespace legalagreement-common-enum inspect each of the changes as follows:
1.	`ContractualSupplementEnum` changed to `ContractualSupplementTypeEnum`
2.	Removal of `LegalAgreementNameEnum` and all its items listed

In namespace legalagreement-common-func inspect each of the changes as follows:
1.	Change of reference from `RelatedAgreement` to `LegalAgreement`
2.	Change of reference from LegalAgreementType to ‘LegalAgreementIdentification’

In namespace `legalagreement-csa-enum` inspect each of the changes to the  enumeration `CreditSupportAgreementTypeEnum` list as follows:
1.	removal of publisher, vintage and governing law of retain items renamed
2.	removal of enumerations within the list not required (margin provisions and 2013,2014 Standardised CSAs)
3.	Repositioning of AcadiaSoft synonym relating to CSA

In namespace legalagreement-csa-type inspect the changes outlined as follows:
1.	In data type ‘CreditSupportAgreementType’ removal of 2 attributes `date` and `identifierValue` these are not required as both of these exist in data type `LegalAgreementBase`

In namespace legalagreement-master-enum inspect the changes outlined as follows:
1.	removal of enumeration within `MasterAgreementTypeEnum` list not required (AFB)
2.	Rename enumeration `ISDA` to `ISDAMaster`

In namespace legalagreement-master-type, inspect the changes outlined as follows:
1.	in data type `CreditSupportDocumentElection` there is a change to the attribute ` creditSupportDocument` to point to `LegalAgreement` instead of `RelatedAgreement`

In namespace synonym-cdm-event, inspect the changes outlined as follows:
1.	Removal of `RelatedAgreement` synonym

In namespace synonym-cdm-fpml, inspect the changes to synonyms outlined as follows:
1.	Removed `ContractualMatrix`  (publication date)
2.	Removed ` ContractualTermsSupplement`
3.	Removed translations related to (date and identifier) in `CreditSupportAgreement`
4.	Removed `RelatedAgreement`
5.	Removed `DocumentationIdentification`
6.	`LegalAgreementType` changed to `LegalAgreeementIdentification` and `name` changed to `agreementName`
7.	Synonym added `masterAgreementType` - `masterAgreement`
8.	Synonym related to `CreditSupportAnnex` adjusted and ‘MasterAgreement’ removed
9.	`ContractualSupplementsEnum` changed to `ContractualSupplementTypeEnum`
10.	Removed ` CreditSupportAgreementTypeEnum`
11.	Removed translation related to (AFB) and adjusted ISDA to ISDAMaster in `MasterAgreementTypeEnum`

In namespace synonym-cdm-isda-create, inspect the changes to synonyms outlined as follows:
1.	Removed `RelatedAgreement`
2.	`LegalAgreementType` changed to `LegalAgreeementIdentification` and `name` changed to `agreementName`
3.	Addition of synonym `AgreementName` with `clauseLibrary`, `securityagreement` and `masterAgreementType`
4.	Removal of synonym `LegalAgreementNameEnum` and all the translations
5.	Addition of synonym `CreditSupportAgreementTypeEnum` with translations for `CreditSuportDeed`, `CreditSupportAnnex`, `CollateralTransferAgreement`

_Review Directions_

In the CDM Portal, select Ingestion and review the following samples with documentation:

Master Agreement, Master Confirmation and Confirmation
- fpml-5-10/products/credit/cdx-index-option.xml

Master Agreement and Confirmation
- fpml-5-10/products/credit/cd-ex01-long-asia-corp-fixreg-versioned.xml

Master Agreement and Credit Support Agreement
- events/novation-both-parties.xml
