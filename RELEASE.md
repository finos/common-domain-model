# *Legal Agreement Model - Features to categorise CSA documents*

_What is being Released_

This change allows the explicit categorisation of the ISDA Credit Support documents. This is particularl helpful for documents published since 2016 that currently carry the margin type (Variation or Initial) only in the agreement name.

_What is being released?_

* New attribute `creditSupportAgreementMarginType ` added to data type name `AgreementName` 
* Conditions added to ensure that a CSA margin type is only specified if a credit support agreement type is specified as an agreement name, and it published year `vintage` is > = 2016
* A new enumeration list `CreditSupportAgreementMarginTypeEnum` with options for `VariationMargin` and `InitialMargin`

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the addition of `creditSupportAgreementMarginType` as an attribute to the data type `AgreementName`.

Please also inspect the related conditions that have been updated in the model named `CSAMarginType` under the data types listed here:

* `LegalAgreementIdentification`
* `AgreementName`

Inspect the associated enumeration list `CreditSupportAgreementMarginTypeEnum` and its contents `VariationMargin`.

