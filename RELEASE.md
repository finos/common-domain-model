# Product Mapping Extension Release Notes

**_Background_**

This release extends the FpML to CDM model to model mappings for various Rates and Credit products.

**_What is being released?_**

The enhancing of the credit default swap representation by including a bond into the reference obligation and the mapping of several FpML elements regarding Credit and Rates asset classes.

_Types_

**base-staticdata-asset-common-type**
- Added the elements `couponRate` and `maturity` to the `Bond` type.

**product-asset-type**
- Added the element `bond` of type `Bond` to the `ReferenceObligation` type in order to enhance the credit default swap representation.

_Synonyms_

**synonym-cdm-fpml**

- Added mapping coverage for the `capRateSchedule` FpML element.
- Added mapping coverage for the `schema` of the FpML element `dayCountFraction`.
- Added mapping coverage regarding IR swap trades for the following elements: `floatingRateIndex` and `knownAmountSchedule`.
_Review Directions_

In the CDM Portal, select Ingestion and review the samples below:

- fpml-5-10 > products > equity > 
- fpml-5-10 > products > credit > 
- fpml-5-10 > products > rates > 
