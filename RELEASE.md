# *Legal Agreement Extension, to allow user to state ‘CSA Margin Type’ as part of Agreement Name*

_What is being Released_

ISDA published Credit Support documents from 2016 onwards have the margin type (Variation or Initial) detailed within the agreement name. For various use cases the implementors of CDM will need to be able to define this information. The following extensions in the legal agreement model will enable this: 

* New attribute `creditSupportAgreementMarginType ` added to data type name `AgreementName` 
* Conditions added to ensure that a CSA margin type is only specified if a credit support agreement type is specified as an agreement name, and it published year `vintage` is > = 2016
* A new enumeration list `CreditSupportAgreementMarginTypeEnum` with options for `VariationMargin` and `InitialMargin`

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the addition of `creditSupportAgreementMarginType` as an attribute to the data type `AgreementName`.

Please also inspect the related conditions that have been updated in the model named `CSAMarginType` under the data types listed here:

* `LegalAgreementIdentification`
* `AgreementName`

Inspect the associated enumeration list `CreditSupportAgreementMarginTypeEnum` and its contents ‘VariationMargin`.

