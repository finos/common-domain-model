# *PartyRoleEnum - Add new PartyRoleEnum value MarginAffiliate*

_Background_

New DTCC field required by CFTC 3.2, specific to Collateral. To support this, the `PartyRoleEnum` is extended by adding the value `MarginAffiliate`. The `PartyRoleEnum` originates from the FpML `partyRoleScheme`, and this role is already published in section 4 of the FpML coding scheme. Therefore, it needs to be added to the CDM to maintain alignment.

_What is being released?_

Add a new enumerated value `MarginAffiliate` to `PartyRoleEnum` with definition: “Margin affiliate as defined by U.S. margin and capital rules §23.151.”

_Review Directions_

Changes can be reviewed in PR: [#4205](https://github.com/finos/common-domain-model/pull/4205)

# _Ingestion - FpML model to CDM model Ingest_

_Background_

As outlined in the [#4031](https://github.com/finos/common-domain-model/issues/4031), the ingest functions for FpML Confirmation to CDM can be contributed to the CDM 6 version.

_What is being released?_

This release contains functions to ingest data from the FpML Confirmation model into CDM. This PR includes:

- Ingest functions for mapping FpML Confirmation to CDM
- Regression tests (test packs and expected results)
- `FpML as Rune` model as an upstream dependency

_Ingest functions_

Ingest functions have been written to map the attributes from a `FpML as Rune` model type to a CDM type.  For example, the function `cdm.ingest.fpml.confirmation.message.Ingest_FpmlConfirmationToTradeState` maps from the `FpML as Rune` type `fpml.confirmation.Document` to the CDM type `cdm.event.common.TradeState`.

```
func Ingest_FpmlConfirmationToTradeState:
    [ingest XML]
    inputs:
        fpmlDocument fpml.Document (0..1)
    output:
        tradeState TradeState (0..1)
```

These functions can be found in multiple namespaces within `cdm.ingest.fpml`.

_Regression tests - test packs and expectations_

The mapping functions have been validated using the same ingestion test packs as the existing Synonym ingestion, and provide an equivalent mapping coverage.

The test packs and expectations for the FpML Confirmation Ingest functions can be found in the folder `ingest`.

_`FpML as Rune` model_

The FpML Confirmation schemas (.xsd) have been imported into a `FpML as Rune` model, where each schema type has a corresponding Rune data type.  e.g. FpML `<dataDocument>` has been imported into the model as type `fpml.confirmation.DataDocument`.

This model is hosted as a public Github repository, see [FpML as Rune](https://github.com/rosetta-models/rune-fpml).  `FpML as Rune` is an upstream dependency of CDM.

_Mapping Diagnostics_

_Background_

It has been raised that there is some ambiguity with the attribute descriptions under the `ConcentrationLimitTypeEnum`.
The word portfolio could mean several things; therefore, the CDM Collateral Working Group agreed to add additional language across the descriptions to ensure they indicate the collateral schedule being listed in the collateral criteria and avoid misinterpretation.

_What is being released?_

Updates to descriptions for `ConcentrationLimitTypeEnum` listings to remove 'portfolio' and replace this with 'eligible collateral schedule' where relevant and required.

_Review Directions_

The changes can be reviewed in PR: [#4027](https://github.com/finos/common-domain-model/pull/4027)

# *FpML Ingest - Volatility Swap Transaction Supplement*

_Background_

Ingest functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps Volatility Swap Transaction Supplement products, as per [#4261](https://github.com/finos/common-domain-model/issues/4261).

Updates to `MapVolatilityLegToPerformancePayout` function to map:
- priceQuantity
- settlementTerms
- observationTerms
- valuationDates
- returnTerms

This release also removes unused imports in the `cdm.ingest.fpml.confirmation` and associated namespaces.

_Review Directions_

Changes can be reviewed in PR: [#4255](https://github.com/finos/common-domain-model/pull/4255)

## Event Model - Empty Value Handling Updates

*Background*

An upcoming DSL release has found a number of areas where the use of `empty` in validation functions and conditions was not being handled correctly. This change contains fixes that prepare the model for the upcoming DSL release.

*What is being released?*

The following types have been updated:

- Instruction
    - Update condition `NewTrade` to return a valid status when `primitiveInstruction -> execution` is absent and `before` exists.

*Review Directions*

Changes can be reviewed in PR: [#4236](https://github.com/finos/common-domain-model/pull/4236)

# *FpML Ingest - Dividend Swap Transaction Supplement*

_Background_

Ingest functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps Dividend Swap Transaction Supplement products, as per [#4262](https://github.com/finos/common-domain-model/issues/4262).

Updates to mapping of Dividend Swap Transaction Supplement:
- priceQuantity
- settlementTerms
- dividendPayoutRatio
- returnTerms
- dividendValuationDate

_Review Directions_

Changes can be reviewed in PR: [#4271](https://github.com/finos/common-domain-model/pull/4271)
