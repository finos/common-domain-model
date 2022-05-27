# *Product Model - FpML mappings for Commodity and Credit*

_What is being released?_

This release fixes various FpML product synonym mapping issues for commodity and credit samples.

- _Commodity_ - synonyms added to map FpML `commoditySwaption` samples that were previously unmapped. 
- _Credit_ - synonyms updated to fix the mapping of FpML `periodicPayment` elements into `paymentDates->paymentDateSchedule->interimPaymentDates->periodicDates`.

_Review Directions_

In the CDM Portal, select Ingestion and review the samples specified below.

* fpml-5-10/incomplete-products/commodity-derivatives
  * com-ex22-physical-gas-option-multiple-expiration.json
  * com-ex23-physical-power-option-daily-expiration-efet.json 
  * com-ex29-physical-eu-emissions-option.json
  * com-ex31-physical-us-emissions-option.json
  * com-ex47-physical-eu-emissions-option-pred-clearing.json

* fpml-5-10/incomplete-products/credit-derivatives (all samples)
