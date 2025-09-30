# *FpML Ingestion - FX Payer and Receiver*

_Background_

This release fixes the FpML ingestion (based on synonyms) mapping of payer and receiver for FX products, as per GitHub issue [#4039](https://github.com/finos/common-domain-model/issues/4039).

_What is being released?_

Update to the FpML synonym mappings for `SettlementPayout` attributes `payer` and `receiver` to correctly correspond the exchange rate quote basis.

_Review Directions_

In Rosetta, select the Ingest tab, select `FpML_5_Confirmation_To_TradeState` and review the following FpML samples:

- fx-ex01-fx-spot.xml
- fx-ex02-spot-cross-w-side-rates.xml
- fx-ex03-fx-fwd.xml
- fx-ex05-fx-fwd-w-ssi.xml
- fx-ex07-non-deliverable-forward.xml
- fx-ex08-fx-swap.xml
- fx-ex26-fxswap-multiple-USIs.xml
- fx-ex28-non-deliverable-w-disruption.xml
- fx-ex29-fx-swap-with-multiple-identifiers.xml

Changes can be reviewed in PR: [#4063](https://github.com/finos/common-domain-model/pull/4063)

# _Collateral Model - Updated descriptions for ConcentrationLimitTypeEnum_

_Background_

It has been raised that there is some ambiguity with the attribute descriptions under the `ConcentrationLimitTypeEnum`.
The word portfolio could mean several things; therefore, the CDM Collateral Working Group agreed to add additional language across the descriptions to ensure they indicate the collateral schedule being listed in the collateral criteria and avoid misinterpretation.

_What is being released?_

Updates to descriptions for `ConcentrationLimitTypeEnum` listings to remove 'portfolio' and replace this with 'eligible collateral schedule' where relevant and required.

_Review Directions_

The changes can be reviewed in PR: [#4027](https://github.com/finos/common-domain-model/pull/4027)
