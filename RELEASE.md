# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-bundle` and `DSL` dependency.

Version updates include:
- `rosetta-bundle` 11.6.0: Dependencies migrated to Maven Central
- `rosetta-bundle` 11.6.2: FpML coding scheme infrastructure update to support configurable coding scheme matching for Prod and Dev versions
- `rosetta-bundle` 11.7.0: Java compilation performance improvements
- `DSL` 9.8.5: Java compilation performance improvements. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.8.5

_Review directions_

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2932

# *Product Model - FpML Mapping Update*

_Background_

This release extends the FpML mapping coverage for FRAs and Commodity products.

_What is being released?_

- FpML synonyms and mapper updated to map FRA floating leg payment dates from the FpML index tenor
- FpML synonyms added to map Commodity delivery date parameter `deliveryNearby`

_Review Directions_

In Rosetta, select the Translate tab and review the following samples:

- fpml-5-10 > products > rates > ird-ex08-fra.xml
- fpml-5-10 > products > rates > ird-ex08-fra-no-discounting.xml
- fpml-5-10 > products > commodity > com-ex41-oil-asian-barrier-option-strip.xml

Changes can be reviewed in PR [#2936](https://github.com/finos/common-domain-model/pull/2936)