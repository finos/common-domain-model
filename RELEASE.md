# *Legal Agreement Model - Features to categorise CSA documents*

_What is being released?_

This change allows the explicit categorisation of the ISDA Credit Support documents. This is particularly helpful for documents published since 2016 that currently carry the margin type (Variation or Initial) only in the agreement name. The adjustments include:

* New attribute `creditSupportAgreementMarginType ` added to data type name `AgreementName` 
* Conditions added to ensure that a CSA margin type is only specified if a credit support agreement type is specified as an agreement name, and it published year `vintage` is > = 2016
* A new enumeration list `CreditSupportAgreementMarginTypeEnum` with options for `VariationMargin` and `InitialMargin`

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the addition of `creditSupportAgreementMarginType` as an attribute to the data type `AgreementName`.

Please also inspect the related conditions that have been updated in the model named `CSAMarginType` under the data types listed here:

* `LegalAgreementIdentification`
* `AgreementName`

Inspect the associated enumeration list `CreditSupportAgreementMarginTypeEnum` and its contents `VariationMargin`.

# Collateral Model - Features to link transactions and collateral portfolios 

_What is being Released_

This change represents the association between a trade, the corresponding collateral portfolios and balances as prescribed by underlyig legal agreements (e.g. IM/VM CSA). The following is included:

* New attributes added to data type `Collateral` for `portfolioIdentifier` and `collateralPortfolio`. This allows users to identify collateral portfolios related to a trade and to list the collateral components and resulting balances.
* New attribute `payerReceiver` added to data type `CollateralBalance`. This allows the representation of both the Payer Receiver (party1 or party2) and the Collateral direction (posted or received)
* New attribute `collateralAgreement` added to data type `CollateralPortfolio`. This allows the direct association of a portfolio with a collateral agreements.
* The data type `CollateralPortfolio` has been made a `[root Type]` to allow for independent use in the model

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the additions laid out above across the following data types:

* `Collateral`
* `CollateralBalance`
* `CollateralPortfolio`
