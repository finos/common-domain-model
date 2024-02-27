# _CDM Model - Settlement Type Mapping Fix_

_Background_

It had been observed that some of the ingested FpML samples in Rosetta were not correctly translating the settlement type of the trade in CDM. Specifically, the field `settlementTerms->settlementType` in CDM was not being populated, despite the incoming FpML samples containing information about the settlement type. This release aims at correcting this mismatch between FpML and CDM. In turn, this will improve the reporting capabilities of the DRR field `DeliveryType`, given that several samples which were previously not reporting this field will now contain the necessary information to do so.

_What is being released?_

- Updates in the synonym mappings have been incorporated to populate the `settlementType` field in CDM whenever FpML contains the fields `cashSettlement`, `amount->cashSettlement`, `tradeHeader->productSummary->settlementType`, or `genericProduct->settlementType`.

_Translate_

- Added `[hint "productSummary"]` to the `tradeLot` in the `TradableProduct` synonym.
- Added `[value "creditDefaultSwap" , "creditDefaultSwap" path "creditDefaultSwapOption" , "creditDefaultSwapOption", "tradeHeader"]` to the `tradeLot` in the `TradableProduct` synonym.
- Added `[hint "settlementType"]` to the `settlementTerms` in the `PriceQuantity` synonym.
- Added `[set to SettlementTypeEnum -> Cash when "cashSettlement" exists]` to the `settlementType` in the `SettlementBase` synonym.
- Added `[set to SettlementTypeEnum -> Cash when "settlementType" = "Cash"]` to the `settlementType` in the `SettlementBase` synonym.
- Added `[set to SettlementTypeEnum -> Cash when "amount->cashSettlement" = True]` to the `settlementType` in the `SettlementBase` synonym.
- Added `[set to SettlementTypeEnum -> Physical when "settlementType" = "Physical"]` to the `settlementType` in the `SettlementBase` synonym.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2728

# _Product Model - Commodity Forwards_

_Background_

This release provides qualification support for Commodity Forward products, which were not supported until now. The representation of commodity forwards pictures two possible kinds of forwards: fixed price forwards consisting in the combination of a `fixedPricePayout` and a `forwardPayout`; and floating price forwards consisting in the combination of a `commodityPayout` and a `forwardPayout`. The `forwardPayout` represents the physical delivery of the commodity while the other payout represents its pricing and the payment of a monetary amount in exchange.

_What is being released?_

- Added the qualifying function `Qualify_Commodity_Forward`.
- Updated the qualifying function `Qualify_AssetClass_Commodity` in order to consider commodity forwards.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2719

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-bundle` and `rosetta-dsl` dependencies.

Version updates include:
- `rosetta-bundle` 10.13.4: FpML Coding schema updated.
- `rosetta-dsl` 9.6.1: DSL bug fix for handing null values. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.6.1.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR: [#2708](https://github.com/finos/common-domain-model/pull/2708) / [#2729](https://github.com/finos/common-domain-model/pull/2729)
