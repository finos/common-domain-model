# Product Model - Party Contact Information

**_Background_**

This release extends the Party model to include Party Contact Information.

**_What is being released?_**

The `ContactInformation` data type has been added to `Party` and `NaturalPerson` to allow representation of Party address related information.

The `PartyContractInformation` data type has been removed from the CDM model.  The data type handles concepts specific to Regulatory Reporting that will be handled in the DRR model.

Synonym mappings have been updated to reflect the model changes.

_Review Directions_

In the CDM Portal, select Textual Browser and reviw the data types above.

In the CDM Portal, select Ingestion and review the samples below:

- isda-create > test-pack > production > 2018-im-csa-ny-law/2.2 > tp2-metadata2_cdm
- fpml-5-10 > incomplete-products > loan > Loan_Bulk_ex100

# Product Model - Product Identification Synonym Mapping

**_Background_**

This release extends the FpML to CDM synonym mappings for product identification.

**_What is being released?_**

The enhancing of the Product Identification mapping to handle a product description and the identification of a CFI code.

_Types_

**base-staticdata-asset-common-enum**
- Added the enumeration `Name` to the enumeration list `ProductIdTypeEnum`

_Synonyms_

**synonym-cdm-fpml**

- Added mapping coverage for the `description` FpML element.
- Added mapping coverage for an `externalProductTypeSource` of `CFI`.

_Review Directions_

In the CDM Portal, select Ingestion and review the samples below:

- fpml-5-10 > products > equity > eqd-ex01-american-call-stock-long-form
- fpml-5-10 > products > credit > cdm-cds-ref-ob-versioned

# Product Model - Interest Rate Product Qualification

**_Background_**

This release updates the Interest Rate Product Qualification rules to more accurately qualify OIS transactions.

**_What is being released?_**

The composable qualification rule `Qualify_Transaction_OIS` has been updated to include additional OIS Floating Rate Options so that Interest Rate Swap OIS contracts are qualified more accurately.

_Review Directions_

In the CDM Portal, select Textual Browser and reviw the function above.

In the CDM Portal, select Ingestion and review the samples below:

- fpml-5-10 > products > rates > NDS-INR-uti
