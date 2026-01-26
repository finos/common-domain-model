# Product Model - Interest Rate and Equity Qualification updates
_Background_

Currently, some FX Products are qualifying as Interest rate, Equity and FX. This is due to a minor issue with the Interest Rate and Equity Qualification Function.

_What is being released?_

This release includes a modification to the `Qualify_AssetClass_InterestRate` and `Qualify_AssetClass_Equity` logic to avoid qualifying FX Products as Interest Rate or Equity.

_Review Directions_

Changes can be reviewed in PR: [#4377](https://github.com/finos/common-domain-model/pull/4377)

# Product Model - Barrier Options Cardinality Updates

_Background_

Barrier Options can have multiple knock-ins and knock-outs which are not supported with the current cardinality. The cardinality of the `knockIn` or `knockOut` / `barrierCap` or `barrierFloor` attributes is currently `(0..1)`.

_What is being released?_

Relaxing the cardinality to `(0..*)` to handle multiple `knockIn` or `knockOut` / `barrierCap` or `barrierFloor`.

_Review Directions_

Changes can be reviewed in PR: [#4358](https://github.com/finos/common-domain-model/pull/4358)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the DSL dependency, and third-party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

Version updates include:
- `DSL` `9.75.1` Performance improvements and bug fix. See DSL release notes: [9.75.1](https://github.com/finos/rune-dsl/releases/tag/9.75.1)
- `DSL` `9.75.0` Suppress warnings annotation. See DSL release notes: [9.75.0](https://github.com/finos/rune-dsl/releases/tag/9.75.0)
- `DSL` `9.74.1` Fix usage of `default` with multi-cardinality. See DSL release notes: [9.74.1](https://github.com/finos/rune-dsl/releases/tag/9.74.1)
- `DSL` `9.74.0` Fix `empty` meta coercion. See DSL release notes: [9.74.0](https://github.com/finos/rune-dsl/releases/tag/9.74.0)
- `DSL` `9.73.0` Clean up DSL warnings. See DSL release notes: [9.73.0](https://github.com/finos/rune-dsl/releases/tag/9.73.0)
- `DSL` `9.72.0` Fix for serialisation. See DSL release notes: [9.72.0](https://github.com/finos/rune-dsl/releases/tag/9.72.0)
- `DSL` `9.71.0` Fixes incorrect treatment of empty for boolean resolution, equality, and implicit `else`. See DSL release notes: [DSL 9.71.0](https://github.com/finos/rune-dsl/releases/tag/9.71.0)

Version updates include:

The PairOff qualification function now correctly qualifies. See updated sample in `src/main/resources/cdm-sample-files/functions/repo-and-bond/pair-off-output.json`

No expectations are updated as part of this release.

Third-party software library updates:

- `npm/qs` upgraded from version 6.13.0 to 6.14.1, see [GHSA-6rw7-vpxm-498p](https://github.com/advisories/GHSA-6rw7-vpxm-498p) for further details

_Review Directions_

The changes can be reviewed in PR: [#4315](https://github.com/finos/common-domain-model/pull/4315) && [#4310](https://github.com/finos/common-domain-model/pull/4310)

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

