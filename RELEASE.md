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

