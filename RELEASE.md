# Product Model - FpML Mappings

_Background_

This release updates and extends the FpML mapping coverage for the product model.

_What is being released?_

* Mappings added to populate CDM attribute `SettlementBase -> settlementType` with code `Cash` or `Physical` when `nonDeliverableSettlement` or `physicalExercise` are present on the FpML input, respectively
* Mappings added to populate CDM attribute `SettlementBase -> settlementCurrency` with FpML element `entitlementCurrency`
* Mappings added to populate CDM attribute `productIdentifier` when the  instrument is a generic product
* Mappings added to populate CDM attributes `primaryAssetClass` and `secondaryAssetClass` when the  instrument is a generic product
* Mappings added to populate CDM attribute `TransferExpression -> priceTransfer` with code `Upfront` when payment type is `Additional Payment` and code `Novation` when the input is a novation
* Mappings added to populate CDM attributes `effectiveDate` and `terminationDate` for generic products
* Mappings added to populate CDM attribute `optionPayout` when the generic product is an option
* Mappings added to populate CDM attribute `Product -> contractualProduct` for generic products
* Mappings added to populate CDM attribute `AveragingCalculationMethod`
* Mappings updated for CDM attribute `PayerReceiver`

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples: 

* `fpml-5-10 > products > fx > fx-ex07-non-deliverable-forward`
* `fpml-5-10 > products > fx > fx-ex28-non--deliverable-w-disruption`
* `fpml-5-10 > products > rates > bond-option-uti`
* `fpml-5-10 > products > rates > cb-option-usi`
* `fpml-5-10 > incomplete-products > bond-options > bond-option`
* `fpml-5-10 > incomplete-products > bond-options > cb-option`
* `fpml-5-10 > incomplete-products > bond-options > cb-option-2`
* `fpml-5-10 > incomplete-products > commodity-derivatives > com-ex22-physical-gas-option-multiple-expiration`
* `fpml-5-10 > incomplete-products > commodity-derivatives > com-ex23-physical-power-option-daily-expiration-efet`
* `fpml-5-10 > incomplete-products > commodity-derivatives > com-ex29-physical-eu-emissions-option`
* `fpml-5-10 > incomplete-products > commodity-derivatives > com-ex31-physical-us-emissions-option`
* `fpml-5-10 > incomplete-products > commodity-derivatives > com-ex47-physical-eu-emissions-option-pred-clearing`
