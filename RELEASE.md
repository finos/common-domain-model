# _Product Model - FpML Mapping - Commodity Forwards_

_What is being released?_

This release extends the FpML mapping coverage for Commodity Forwards.

- FpML `commoditySwap` legs `coalPhysicalLeg`, `electricityPhysicalLeg`, `environmentalPhysicalLeg`, `gasPhysicalLeg`, `oilPhysicalLeg` have been mapped into the model as a `ForwardPayout` leg
- The `payerPartyReference` and `receiverPartyReference` has been mapped into `ForwardPayout->payerReceiver`
- The `commodityClassification` has been mapped into `ForwardPayout->underlier->commodity->productTaxonomy` as a reference

_Review directions_

In Rosetta, open the Translate tab and review the `FpML 5.13 > processes` test pack samples:

- msg-ex69-execution-advice-commodity-swap-classification-new-trade-esma-emir-refit.xml
- msg-ex69-commodity-swap-coal-physical-leg.xml
- msg-ex69-commodity-swap-electricity-physical-leg.xml
- msg-ex69-commodity-swap-environmental-physical-leg.xml
- msg-ex69-commodity-swap-gas-physical-leg.xml

The changes can be reviewed in PR: [#2803](https://github.com/finos/common-domain-model/pull/2803)

# _Infrastructure - FpML 5.13 Ingestion Test Pack_

_What is being released?_

This release adds an ingestion test pack containing FpML 5.13 WD3 xml files from [www.fpml.org/spec/fpml-5-13-3-wd-3/](https://www.fpml.org/spec/fpml-5-13-3-wd-3/) examples download.

There are no changes to the model. Ingestion test expectations have been added for the new FpML 5.13 test pack.

_Review directions_

In Rosetta, open the Translate tab and review test packs:

- fpml-5-13/products
- fpml-5-13/incomplete-products
- fpml-5-13/processes
- fpml-5-13/incomplete-processes

The changes can be reviewed in PR: [#2797](https://github.com/finos/common-domain-model/pull/2797)

# _Infrastructure - Remove Unused Folders_

_What is being released?_

This release removes files and folders that were previously used by the CDM Portal.

Removed folders:
- distribution
- rosetta-source/src/main/resources/calculation-test-cases
- rosetta-source/src/main/resources/cdm-sample-files/event-sequences

There are no changes to the model.

_Review directions_

The changes can be reviewed in PR: [#2800](https://github.com/finos/common-domain-model/pull/2800)
