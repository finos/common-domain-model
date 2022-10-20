# _Product Model - FpML Synonym Mapping Enhancements_

_What is being released?_

This release fixes various FpML product synonym mapping issues identified through a review of unmapped fields.

- Product Identification - synonyms added to map `productTaxonomy` and `productIdentifier` more accurately
- Various other mappings have been added to resolve validation issues previously appearing

_Review Directions_

In the CDM Portal, select Ingestion and review the samples below

- products > rates > `bond-option-uti` - `productIdentifier` no longer incorrectly mapped on to bond option
- incomplete-products > commodity-derivatives > `com-ex29-physical-eu-emissions-option` - `productTaxonomy` correctly mapped
