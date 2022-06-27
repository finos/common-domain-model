# Product Mapping Extension Release Notes

**_Background_**

This release extends the FpML to CDM model to model mappings for various Rates and Credit products.

**_What is being released?_**

The enhancing of the credit default swap representation by including a bond into the reference obligation and the mapping of several FpML elements regarding Credit and Rates asset classes.

_Types_

**base-staticdata-asset-common-enum**
- Added the enumeration `Name` to the enumeration list `ProductIdTypeEnum`

_Synonyms_

**synonym-cdm-fpml**

- Added mapping coverage for the `description` FpML element.
- Added mapping coverage regarding FRA trades for the following elements: `primaryAssetClass`, `productId` and `productType`.
- Added mapping coverage regarding CD swap trades for the following elements: `bond` and `indexReferenceInformation.seniority`.
- Added mapping coverage regarding CD swaption trades for the following elements: `primaryAssetClass`, `productId` and `strike`.

_Review Directions_

In the CDM Portal, select Ingestion and review the samples below:

- fpml-5-10 > products > equity > 
- fpml-5-10 > products > credit > 
- fpml-5-10 > products > rates > 
