# _Legal Documentation Model - New ContractualDefinitionsEnum value to support the 2022 ISDA Verified Carbon Credit Transactions Definitions_

_Background_

The International Swaps and Derivatives Association, Inc. published in 2022 the ISDA Verified Carbon Credit (VCC) Transactions Definitions (see https://www.isda.org/book/2022-isda-verified-carbon-credit-transaction-definitions/).

This release includes the reference to the ISDA VCC Transaction Definitions by including a new value to the existing `ContractualDefinitionsEnum`.

_What is being released?_

New `ContractualDefinitionsEnum` value:
- Add a new value `ISDA2022VerifiedCarbonCredit`, in line with the existing values.
- The annotation of the value is: ISDA 2022 Verified Carbon Credit Transactions Definitions.
- Correct a typo in the annotation of the `ISDA2023DigitalAssetDerivatives` value, with a year of 2023 instead of 2021 as the value indicates.

_Review directions_

The changes can be reviewed in PR: [#3048](https://github.com/finos/common-domain-model/pull/3048)

_Backward-incompatible changes_

The change is backward compatible.

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.16.3: bug fix for tabulators and validation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.3
- `DSL` 9.16.5: performance improvements for tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.5
- `DSL` 9.16.6: bug fix for meta generation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.6
- `DSL` 9.16.7: bug fix for report generation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.7
- `DSL` 9.17.0: new syntax features. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.17.0
- `DSL` 9.17.1: bug fix for global key generation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.17.1
- `DSL` 9.17.2: bug fix for ingestion id mapping. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.17.2
- `DSL` 9.18.0: new syntax features. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.18.0
- `DSL` 9.18.1: memory improvements. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.18.1


_Review directions_

The changes can be reviewed in PR: [#3126](https://github.com/finos/common-domain-model/pull/3126) [#3142](https://github.com/finos/common-domain-model/pull/3142)