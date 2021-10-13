# *Product Model - FpML Synonym Mapping for Forward Rate Agreements*

_What is being released?_

This release updates the FpML mappings for FRAs to cover `PriceQuantity`.

_Details_

FpML represents FRAs in a single `fra` xml element which is currently mapped into separate fixed and floating `InterestRatePayout` legs.  This release adds logic to also map the `PriceQuantity` instances that correspond to the fixed and floating `InterestRatePayout` components of the product.  

_Review Directions_

In the CDM Portal, select the Ingestion Panel and review the following samples:

- fpml-5-10 > products > ird-ex08-fra.xml
- fpml-5-10 > products > ird-ex08-fra-no-discounting.xml
