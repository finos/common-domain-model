# _Product Model - Asset Refactoring: Product, SettlementPayout, Underliers_

WORK IN PROGRESS

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.  

This release includes the third tranche of changes (of three planned tranches in CDM 6) to implement the refactored model. It introduces some significant refactoring of the Product structure in to the model.

_What is being released?_

Product Refactoring:
- This release completes the refactoring of the major financial product data types, that is `Asset`, `Observable`, and `Index`, and combines their use in a new structure for financial products.
- The new data type `NonTransferableProduct` has replaced the former `ContractualProduct` and is the main "product" data type used on a trade; it appears as the attribute `product` on the data type `TradableProduct`.
- On a `Trade`, all financial products should be composed into the `EconomicTerms` of a `NonTransferableProduct`.
- *Separate PR but included here for completeness* The data type `payout` is now a choice construct with the consequence that references to specific instances of a specific payout should refer to the capitalised data type name rather than an attribute, for example `economicTerms -> payout -> performancePayout` becomes `economicTerms -> payout -> PerformancePayout`. This has large impact in terms of the number of changes in this PR.

Underliers:
- An `Underlier` represents the financial product that will be physically or cash settled.
- Whereas all underliers were previously defined to use data type `product`, this has now been improved so that they can also be an `Observable` when the case warrants it.
- `Underlier` is modelled as a choice data type, that is, it can either be an `Observable` or a `Product`.
- A `Product` is also a choice type, either a `TransferableProduct` (a type of financial product which can be held or transferred, represented as an Asset with the addition of specific EconomicTerms), or a     NonTransferableProduct (a product that can be traded, as part of a TradableProduct, but cannot be transferred to others).

Product and Trade Hierarchy:
- The `Trade` data type now extends `TradableProduct`; this means that the latter is "hidden" in many uses in the CDM, eg in the graphical view, and one level within the hierarchy is removed when generating JSON.  This change has resulted to updates to 100s of occurences to path accesses within the model (particularly in the Event and Product functions).  For example, the previous access path `tradeState -> trade -> tradableProduct -> tradeLot` has become `tradeState -> trade -> tradeLot`. 
- The financial product hierarchy has also changed so that the previous path `trade -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> InterestRatePayout` has become `trade -> product -> economicTerms -> payout -> InterestRatePayout`.

Product details:
- `Security`
  - Removed the attributes `economicTerms` and `ProductTaxonomy` because these are not appropriate for an `Asset` data type which is standardised. if `economicTerms` are required, these can be added by wrapping `security` into a `TransferableProduct`. The taxonomy of assets is explicitly defined in the type.
- `Index`
  - `Index` is now an `Observable` and the replaces the existing data type `rateOption`.
  - The path `observable -> rateOption` has become `observable -> Index -> FloatingRateIndex`.
  - Data type `CreditIndex` now extends `IndexBase`.

ProductQualification:
- Additional functions have been created to ease with the qualification process: `UnderlierQualification`. `ObservableQualification`.
- Extensive refactoring has been made to the qualification functions to reflect the refactoring, albeit with no changes to the actual logic.
- The qualification for options has been tidied up to be more readable and maintainable in the new model, without change to the functionality.
- The introduction of `SettlementPayout` has been included in the qualification logic.
- The qualification of Foreign Exchange transactions has been updated.
- The securities financing qualification functions have been enhanced to differentiate repos and lending; the new functions are: `Qualify_RepurchaseAgreement`, `Qualify_BuySellBack`, and `Qualify_SecurityLending`.
- A new enumerator data type has been added to support repo qualification: `RepoTypeEnum`.  The corresponding attribute `repoType` was added to `AssetPayout`.

Payouts:
- A new payout type has been created:  `SettlementPayout`
- The existing `ForwardPayout` has been collapsed into `SettlementPayout`; the latter should be used whereever the former was previously.
- *Separate PR but included here for completeness* The `Payout` data type has been refactored to be a `choice` and payouts now have multiple cardinality on `EconomicTerms`.  The conditions that validated the business logic on payouts has been moved to `EconomicTerms`.

Event Model:
- `Create_Exercise` has additional logic to support an option underlier that coan be an `Asset`, a `TransferableProduct` or a `NonTransferableProduct`.
- `Create_Execution` now acts upon a more narrowly defined `NonTransferableProduct` rather than a generic product.
- Additional functions have been created to support events using the new product model:  `Create_NonTransferableProduct`, `Create_TransferableProductFromAsset`, `Create_TransferableProductFromIndex`, `CheckTransferableProduct`, `CheckTradeNotTransferableProduct`, `CreateTradableProduct`.
- The function `NewEquitySwapProduct` now creates a `NonTransferableProduct` not a generic product.
- Event processing has been refactored to handle the new modeling of `TradableProduct`.
- The unused data types `Affirmation` and `Confirmation` have been removed.

Observable:
- The attribute `Observable` has been removed from `ObservationTerms` where it created duplication.

Collateral:
- `AssetIdentifier` replaces `ProductIdentifier` on `AssetCriteria`.
- The function `CheckEligibilityForProduct` now uses `TransferableProduct` not `Product`.

Namespace re-alignments:
- The following data objects have been moved to a more appropriate namespace:
  - `enum PutCallEnum` to cdm.base.staticdata.asset
  - `choice Index` to cdm.observable.asset
  - `type IndexBase` to cdm.observable.asset
  - `type PriceQuantity` to cdm.observable.asset
- The function `InterestRateObservableCondition` has been moved to the cdm.observable.asset function namespace.
 
Deprecated data types which have been removed:
- `IndexReferenceInformation`: replaced with `Index`.

Documentation updates:
- Significant improvements have been made to the pages on Product Model, Event Model and Process Model.
- As well as incorporating the direct changes that result from the refactoring, the pages have been restructured to improve the hierarchy, sections have been resequenced where this improves understanding, and "tips" have been added to highlight important definitions.
- The use case sections on Collateral, Securities Lending and Repos have been updated to reflect the revised modeling.

_Review directions_

The changes can be reviewed in PR: [#3127](https://github.com/finos/common-domain-model/pull/3127)

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- TBA

A full description of the backward-incompatible changes, and how persisted objects should be remapped, will be included in the release notes for the production release of CDM 6.
