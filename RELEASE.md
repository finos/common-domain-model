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

Changes can be reviewed in PR: [#4062](https://github.com/finos/common-domain-model/pull/4062)

# *Product Model - Refactor function UpdateAmountForEachMatchingQuantity*

_Background_

The function `Create_QuantityChange` relies on function `UpdateAmountForEachMatchingQuantity` to update the price and quantity amounts. However, the function is written in Java because historically the DSL syntax did not support some required operations.  Further details on the background context can be found in Issue [#3907](https://github.com/finos/common-domain-model/issues/3907).

_What is being released?_

Refactor function `UpdateAmountForEachMatchingQuantity` from Java into Rune.

_Review Directions_

There is an expectation change in repo-and-bond visualisation test-pack related to an existing issue where the Quantity Change func does not match on Observable, as discussed in Issue [#3907](https://github.com/finos/common-domain-model/issues/3907).

Changes can be reviewed in PR: [#3955](https://github.com/finos/common-domain-model/pull/3955)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.62.0 Java code generation fixes related to setting metadata. See DSL release notes: [DSL 9.62.0](https://github.com/finos/rune-dsl/releases/tag/9.62.0)
- `DSL` 9.63.0 Changes the generated Java pruning algorithm to keep required attributes, even if they are empty. See DSL release notes: [DSL 9.63.0](https://github.com/finos/rune-dsl/releases/tag/9.63.0)
- `DSL` 9.64.0 See DSL release notes: [DSL 9.64.0](https://github.com/finos/rune-dsl/releases/tag/9.64.0)
  * Show error for duplicate attributes in the same type
  * Add support for XML union elements
  * Added workaround for XSD `any` element
  * Reverted to prune required fields, added config to disable pruning
  
Test expectations have been updated accordingly to include required empty model objects that were previously pruned.

_Review Directions_

The changes can be reviewed in PR: [#3944](https://github.com/finos/common-domain-model/pull/3944)
