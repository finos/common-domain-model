# Product Model - Product Level Synonyms

_What is being released?_

Mappings added to populate CDM attribute `SettlementBase -> settlementType` with code `Cash` or `Physical` when `nonDeliverableSettlement` or `physicalExercise` are present on the FpML input, respectively.

Mappings added to populate CDM attribute `SettlementBase -> settlementCurrency` with FpML element `entitlementCurrency`.

Mappings added to populate CDM attribute `productIdentifier` when the  instrument is a generic product.

Mappings added to populate CDM attributes `primaryAssetClass` and `secondaryAssetClass` when the  instrument is a generic product.

Mappings added to populate CDM attribute `TransferExpression -> priceTransfer` with code `Upfront` when payment type is `Additional Payment` and code `Novation` when the input is a novation.

Mappings added to populate CDM attributes `effectiveDate` and `terminationDate` for generic products.

Mappings added to populate CDM attribute `optionPayout` when the generic product is an option.

Mappings added to populate CDM attribute `Product -> contractualProduct` for generic products.

Mappings added to populate CDM attribute `AveragingCalculationMethod`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM portal, select the Translate Tab and run the following samples: `bond option`, `cb option`, `cb option 2`, `com ex22 physical gas option multiple expiration`, `com ex23 physical power option daily expiration efet`, `com ex29 physical eu emissions option`, `com ex31 physical us emissions option`, `com ex47 physical eu emissions option pred clearing`, `fx ex07 non deliverable forward`, `fx ex28 non deliverable w disruption`, `bond option uti` and `cb option usi`.

# *Product Model - Orphan Types clean-up*

_Background_

This release relocates and deletes some unused types in the model and adjusts the corresponding FpML synonym mappings.

_What is being released?_

- Value `MIC` added to enumeration `PartyIdentifierTypeEnum`
- FpML mappings updated for `PartyIdentifierTypeEnum` values `MIC` and `LEI`

_Review Directions_

In Rosetta, select `CDM for Digital Regulatory Reporting` project, then the Translate tab, and review samples in `fpml-5-10 > record-keeping > products > rates` 
