# *Ingestion Framework for FpML - Mapping Coverage: Credit, Equity and Commodity*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage. For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps Credit, Equity and Commodity products, as per [#4453](https://github.com/finos/common-domain-model/issues/4453), [#4454](https://github.com/finos/common-domain-model/issues/4454) and [#4455](https://github.com/finos/common-domain-model/issues/4455).

- Mapping support added for `AssetIdTypeEnum` values `Name` and `REDID` for FpML Credit products
- Mapping of price per option updated for FpML Equity products
- Mapping of `id` to `CommodityPayout` for FpML Commodity products

_Review Directions_

Changes can be reviewed in PR: [#4445](https://github.com/finos/common-domain-model/pull/4445)