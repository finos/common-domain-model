# *FpML Ingestion - Related Party Role*

_Background_

This release fixes the FpML ingestion (based on synonyms) mapping of related party role, as per GitHub issue [#3713](https://github.com/finos/common-domain-model/issues/3713).

_What is being released?_

Update to the FpML synonym mapping logic for `PartyRole` to remove invalid empty mappings.

_Review Directions_

In Rosetta, select the Ingest tab, select `FpML_5_Confirmation_To_TradeState` and review the following FpML sample, `USD-Vanilla-swap.xml`.

Changes can be reviewed in PR: [#4064](https://github.com/finos/common-domain-model/pull/4064)