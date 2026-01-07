# *Ingestion Framework for FpML - Mapping Coverage: Credit Default Swap Option*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage. For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps Credit Default Swap Option products, as per [#4293](https://github.com/finos/common-domain-model/issues/4293).

Updates to mapping of FpML products `creditDefaultSwapOption` and `creditDefaultSwap`:

- `Product` attributes `taxonomy`
- `TradeLot` attributes `priceQuantity`
- `OptionPayout` attributes `exerciseTerms`, `referenceInformation`, `priceQuantity`, `settlementTerms`, `feature`, `underlier`, `optionType` and `strike`

_Review Directions_

Changes can be reviewed in PR: [#4339](https://github.com/finos/common-domain-model/pull/4339)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` `9.71.0` Fixes incorrect treatment of empty for boolean resolution, equality, and implicit `else`. See DSL release notes: [DSL 9.71.0](https://github.com/finos/rune-dsl/releases/tag/9.71.0)

The PairOff qualification function now correctly qualifies. See updated sample in `src/main/resources/cdm-sample-files/functions/repo-and-bond/pair-off-output.json`

_Review Directions_

The changes can be reviewed in PR: [#4310](https://github.com/finos/common-domain-model/pull/4310)

