# _Product Model - Portfolio Return Terms_

_Background_

Some product may contain multiple individual return legs with a need to represent in same trade:
- an aggregated view of the payout in which `underlier->Basket` shall be defined, together with
- multiple return legs respective `underlier->Security` will also exist (that is a single underlier, mostly each being the same as the ones in `Basket->basketConsituent`).
That is notably the case when the portfolio return is of `priceReturn` type, usually in connection with CFD-like business background. As an indication, other business cases may exist where the return at stake is of another kind, say `varianceReturn` or `volatilityReturn`, as part of dispersion strategy.
For the purpose of encompassing all the business cases, `PortfolioReturnTerms` new type is created, which mainly `extends ReturnTerms`, and is added as a new `PerformancePayout` attribute, with an open cardinality (0..*) : that is where to define the multiple individual returns; whereas the aggregated Basket level may exists at `PerformancePayout` level.
This also comes with other minor changes as further detailed below.

_What is being released?_

- creation of new type `PorfolioReturnTerms` which `extends ReturnTerms` with existing types `PayerReceiver` ; also with existing type `PriceSchedule` to `PerformancePayout` used under three attribute names, and `NonNegativeQuantitySchedule` used for one, respectively : : `initialValuationPrice`, `interimValuationPrice`, `finalValuationPrice` and `quantity`
- add the above new type to `PerformancePayout`
- add existing type `PriceSchedule` to `PerformancePayout` used under three attribute names : `initialValuationPrice`, `interimValuationPrice` and `finalValuationPrice` (that is to replace current position in `ReturnTerms`, still in Prod for backward compatibility reasons, but will be [deprecated] at some point)
- add new type `PorfolioBasketConsituent` to `Basket` (that is to enrich current type `Product`, but for backward compatibilty reasons, instead of modifying this type, a new one was created) which `extends Product` with existing types PriceSchedule used under three attribute names, and `NonNegativeQuantitySchedule` used for one, respectively : `initialValuationPrice`, `interimValuationPrice`, `finalValuationPrice` and `quantity`

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in  PR: [#2776](https://github.com/finos/common-domain-model/pull/2776)


# _Product Model - FpML Mapping - Commodity Forwards_

_What is being released?_

This release extends the FpML mapping coverage for Commodity Forwards.

- FpML `commoditySwap` legs `coalPhysicalLeg`, `electricityPhysicalLeg`, `environmentalPhysicalLeg`, `gasPhysicalLeg`, `oilPhysicalLeg` have been mapped into the model as a `ForwardPayout` leg
- The `payerPartyReference` and `receiverPartyReference` have been mapped to `ForwardPayout->payerReceiver`
- The `commodity->commodityClassification` reference has been mapped to `ForwardPayout->underlier->commodity->productTaxonomy`

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

The changes can be reviewed in PR: [#2798](https://github.com/finos/common-domain-model/pull/2798)

# _Infrastructure - Remove Unused Folders_

_What is being released?_

This release removes files and folders that were previously used by the CDM Portal.

Removed folders:
- distribution
- rosetta-source/src/main/resources/calculation-test-cases
- rosetta-source/src/main/resources/cdm-sample-files/event-sequences

There are no changes to the model.

_Review directions_

The changes can be reviewed in PR: [#2799](https://github.com/finos/common-domain-model/pull/2799)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-dsl` dependencies.

Version updates include:
- `rosetta-dsl` 9.7.0: DSL validation and performance enhancements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.7.0.

There are no changes to the model.  The number of expected ingestion validation failures has changed due to changes in the way validation failures are counted.

_Review directions_

The changes can be reviewed in PR: [#2780](https://github.com/finos/common-domain-model/pull/2780)
