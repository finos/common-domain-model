# _Product Model - Settlement Payout Price_

_What is being released?_

This release updates the FpML synonyms to map the price to the `SettlementPayout->priceQuantity->priceSchedule` attribute for FX samples.

_Backwards incompatible changes_

This release removes attributes `SettlementPayoutt->fixedPrice` and `OptionPayout->fixedPrice` as they are duplicates of the existing attributes `SettlementPayout->priceQuantity->priceSchedule` and `OptionPayout->priceQuantity->priceSchedule`.

_Review directions_

In Rosetta, select the Textual Browser and inspect FpML mapping changes in namespace `cdm.mapping.fpml.confirmation.tradestate`.

In Rosetta, select the Ingest tab and review the following FpML samples:

- fx-ex01-fx-spot.xml
- fx-ex02-spot-cross-w-side-rates.xml
- fx-ex03-fx-fwd.xml
- fx-ex05-fx-fwd-w-ssi.xml
- fx-ex07-non-deliverable-forward.xml
- fx-ex08-fx-swap.xml
- fx-ex26-fxswap-multiple-USIs.xml
- fx-ex28-non-deliverable-w-disruption.xml
- fx-ex29-fx-swap-with-multiple-identifiers.xml

In Rosetta, select the Visualisation tab and review the `Repo And Bond > Bond Execution` example:

The changes can be reviewed in PR: [#3250](https://github.com/finos/common-domain-model/pull/3250)