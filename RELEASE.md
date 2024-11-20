# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the rune dependencies.

Version updates include:
- DSL 9.22.0: handle null for `min` and `max` operations. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.22.0
- FpML Coding Scheme `11.25.1`: support for latest version (v2.20).

_Review directions_

In Rosetta, select the Textual Browser and inspect changes due to the FpML code scheme update:
- `FloatingRateIndexEnum` has values added:
    - `EUR_EuroSTR_ICE_Swap_Rate`
    - `IDR_INDONIA`
    - `IDR_INDONIA_OIS_Compound`
    - `PHP_ORR`
    - `USD_SOFR_ICE_Swap_Rate_Spreads`

The changes can be reviewed in PR: [#3261](https://github.com/finos/common-domain-model/pull/3261)