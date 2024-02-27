# _Legal Agreements - Master Agreement Type enumeration - ISDAIIFM_TMA code_

_Background_

In CDM there are not any available codes for Islamic Master Agreements. This contribution adds a new `ISDAIIFM-TMA` code to the `MasterAgreementTypeEnum` to cover the Islamic Derivative Master Agreements in CDM.

_What is being released?_

- Added a new `ISDAIIFM_TMA` code to the MasterAgreementTypeEnum.
- Mapping coverage for this new `ISDAIIFM_TMA` code.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2720

# _Mapping - Settlement Type Mapping Fix_

_Background_

It had been observed that some of the ingested FpML samples in the Rosetta Platform were not correctly translating the settlement type of the trade. Specifically, the field `settlementTerms->settlementType` in CDM was not being populated, despite the incoming FpML samples containing information about the settlement type. This release aims at correcting this mismatch between FpML and CDM. In turn, this will improve the reporting capabilities of the DRR field `DeliveryType`, given that several samples which were previously not reporting this field will now contain the necessary information to do so.

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

# _Mapping - Commodity Classification Coverage_

_Background_

Following the addition of commodity classification structures in the Rosetta Platform, there is now a need to incorporate mappings from the corresponding FpML fields to the new CDM fields related to the classification of commodities.

Specifically, to cover the CDM mappings to:
- `underlier->commodity->productTaxonomy->value->classification->value` and
- `underlier->commodity->productTaxonomy->value->classification->ordinal`

_What is being released?_

- Synonym mappings that map:
    - `commodityClassification->code` to `classification->value`.
    - `commodityClassification->code->commodityClassificationScheme` to `classification->ordinal`.

- The `ordinal` is mapped to values `1`, `2`, or `3` as follows:
    - If `commodityClassificationScheme` = _http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification_, then `ordinal` = 1
    - If `commodityClassificationScheme` = _http://www.fpml.org/coding-scheme/esma-emir-refit-layer-2-commodity-classification_, then `ordinal` = 2
    - If `commodityClassificationScheme` = _http://www.fpml.org/coding-scheme/esma-emir-refit-layer-3-commodity-classification_, then `ordinal` = 3
 
_Mappings_

- `[value "floatingLeg", "oilPhysicalLeg"]` is added to `priceQuantity` in the `TradeLot` synonym.
- `[hint "commodityClassification"]` is added to `commodity` in the `Observable` synonym.
- `[value "code"]` is added to `value` in the `TaxonomyClassification` synonym.
- `[hint "commodityClassification"]` is added to the `observable` in the `PriceQuantity` synonym.
- `[set to 1 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 2 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-2-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 3 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-3-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 1 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-1-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 2 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-2-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 3 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-3-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to TaxonomySourceEnum -> ISDA when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-1-commodity-classification"]` is added to `source` in the `ProductTaxonomy` synonym.
- `[set to TaxonomySourceEnum -> EMIR when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification"]` is added to `source` in the `ProductTaxonomy` synonym.
- `[hint "primaryAssetClass"]`, `[hint "secondaryAssetClass"]`, `[hint "fra"]`, `[hint "creditDefaultSwapOption"]`, `[hint "bondOption"]`, `[hint "commoditySwaption"]`, `[hint "genericProduct"]`, `[hint "productType"]`, and `[value "commodityClassification"]` are added to the `productTaxonomy` in the `ProductBase` synonym.


_Review directions_

In the Rosetta Platform, select the Textual Browser and inspect each of the changes listed above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2734

# _Product Model - Day Count Fraction: RBA_Bond_Basis_

_Background_

The codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` in the CDM enum `DayCountFractionEnum` have been found redundant by definition. The solution to this issue is to merge them into one single code: `RBA_BOND_BASIS`. This also aligns with the FpML representation.

_What is being released?_

- Deprecated the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` and added the code `RBA_BOND_BASIS` in the CDM enum `DayCountFractionEnum`.
- Mapping added to populate the new code with the FpML code `RBA`.

_Review directions_

In Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

PR: https://github.com/finos/common-domain-model/pull/2726

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
