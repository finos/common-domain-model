# _Product Model - Asset Refactoring: Basket, Index, Observable, Foreign Exchange_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.  

This release includes the second tranche of changes (of three planned tranches in CDM 6) to implement the refactored model. It introduces some new data types and makes changes to others; more significant refactoring ofthe Product structure will be introduced in the third release.

_What is being released?_

The word "update" below is to refer to a change to the modelling between this release and the previous release of Asset Refactoring changes (in CDM 6.0-dev.58).

Updates to `AssetBase` data type:
- The `identifier` attribute has been made mandatory.
- The metadata scheme has been removed.
- The attributes `isExchangeListed`, `exchange` and `relatedExchange` have been moved from `InstrumentBase` to `AssetBase`.
- The type of the `exchange` and `relatedExchange` attributes has been updated from `string` to `LegalEntity`.

Updates to `Cash` data type:
- Two new conditions have been added to ensure that `taxonomy` and `exchange` are not used for this asset type.

Changes to `Commodity` data type:
- Now extends from `AssetBase` not `ProductBase`.
- Accordingly, `productTaxonomy` has been replaced by `taxonomy` and the conditions updated.

Changes to `Index` and `IndexBase` data types:
- The attributes `exchange` and `relatedExchange` have been removed from `IndexBase` as they will now be picked up from 'AssetBase`.
- `Index` has been refactored from a `one-of` condition to a choice data type.
- The data type `FloatingIndex` has been renamed `FloatingRateIndex`.
- The documentation in the model on the index-related data types has been improved.
- The qualification logic has been updated to reflect the new modelling of `Index` as an underlier on the relevant functions. 

Updates to `ListedDerivative` data type:
- Updates have been made to the attribute names and types introduced in the previous release to improve modelling and composition.
- The condition on `VarianceReturnTerms` has been updated to reflect the new position of `ListedDerivative` in the product model.

Changes to `Security` data type:
- Now extends from `InstrumentBase` not `ProductBase`.
- Temporary changes made to add `economicTerms` and `productTaxonomy` pending further refactoring in the third phase.

Support for FX Observables:
- The data type `ForeignExchangeRate` has been created and added as a choice to `Index`; it also extends `IndexBase`.
- This new data type contains the same attributes as the existing `FXRateObservable` which has been deprecated.
- The `ExchangeRate` and `ForeignExchange` data types have been deprecated and the deprecated `CrossRate` data type has been deleted.

Refactoring of `Observable`:
- The following data types have been added to `Observable` as new attributes: `Asset`, `Basket`, `Index`.
- The following data types have been removed from `Observable`:  `Commodity` (now available as an `Asset`); `QuotedCurrencyPair` (replaced by the the FX observable data type inside `Index`).
- The unused attribute `optionReferenceType` and its corresponding enumerator `OptionReferenceTypeEnum` have been removed from the model.
- `Observable` now has a `one-of` condition (and will be updated to `choice` in the third phase).
- The two attributes `pricingTime` and `pricingTimeType` on `ObservationTerms` have been renamed `observationTime` and `observationTimeType` respectively.

Changes to `BasketConstituent`:
- `BasketConstituent` now extends from `Observable`, not `Product`.
- Moved from the product namespace to the observable namespace.
- The qualification logic has been updated to reflect the new modelling of `BasketConstituent` as an underlier on the relevant functions.

Updates to `TransferableProduct`:
- The inheritance on this data type has been updated so that it now extends `Asset` but the attributes are unchanged.

Updates to Payouts:
- The documentation in the model on the payout-related data types has been improved.
- The `SettlementCommitment` data type and attribute has been renamed `SettlementPayout`.

_Review directions_

The changes can be reviewed in PR: [#3039](https://github.com/finos/common-domain-model/pull/3039)

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- Changes to the following data types are particularly impactful and have required updates to the mapping synonyms and samples:
  - Commodity
  - Index
  - QuotedCurrencyPair
  - FXRateObservable.

A full description of the backward-incompatible changes, and how persisted objects should be remapped, will be included in the release notes for the last tranche of the asset refactoring.
