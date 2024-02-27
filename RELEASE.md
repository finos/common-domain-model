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

In the Rosetta platform, select the Textual Browser and inspect each of the changes listed above.

Changes can be reviewed in PR:https://github.com/finos/common-domain-model/pull/2735
