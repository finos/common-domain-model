# _Product Model - Asset Refactoring: Asset, Index, Identifier_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.  

This release includes the first tranche of changes to implement the refactored model (the first of three planned tranches in CDM 6).  It introduces some new data types and makes minor changes to others without significant impact to the Product structure itself.

_What is being released?_

New `Asset` data type:
- Introduce the new data type `Asset` which is defined as "something that can be owned and transferred in the financial markets". The data type is implemented using the new Rune DSL feature `choice` that is available in [Release 9.10](https://github.com/finos/rune-dsl/releases/tag/9.10.0).
- Introduce the additional Asset data sub-type called `Instrument`, also using `choice`, defined as "a type of Asset that is issued by one party to one or more others".
- Create the new base class `InstrumentBase` to model common attributes across all `Instrument` data types.
- Introduce the new enumerator `AssetIdTypeEnum`, to define certain identifier sources unique to Assets, as an extension of `ProductIdTypeEnum`.
- Change the inheritance from `ProductBase` to `InstrumentBase` for `Loan`, `ListedDerivative`.
- Add a reference on `Observable` to an `Asset` using an `AssetIdentifier`.

Changes to `Transfer`:
- As `Asset` is defined as something that can be transferred, the modelling of `Transfer` has been refactored to act upon `Asset` rather than `Observable` with a change to `TransferBase`.
- This also results in changes to the `Qualify_SecurityTransfer` function.

Product Model:
- Introduce a new data type on `Payout`: `SettlementCommitment` which models the settlement of an `Asset` for cash.
- Introduce `TransferableProduct` as a type of Product which can be used in a `SettlementCommitment` for a basic cash settled trade of either an `Asset` with or without the addition of specific `EconomicTerms`.
- Define the new `SettlementCommitment` data type to model this new kind of `Payout`.

_Review directions_

The changes can be reviewed in PR: [#3022](https://github.com/finos/common-domain-model/pull/3022)

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- The change in the inheritance for `Loan` and `ListedDerivative` impacts the use of identifiers in these data types.
- The refactoring of `Transfer` to act upon an `Asset` rather than `Observable` impacts the use of the related functions.

Samples and mappings for both have been updated accordingly.
