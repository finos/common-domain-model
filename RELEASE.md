# _Product Model_ - Commodity Payout Underlier

_Background_

The Asset Refactoring initiative (see [#2805](https://github.com/finos/common-domain-model/issue/2805)) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes two minor adjustments following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

In the original Asset Refactoring scope, the `underlier` on `CommodityPayout` was changed from being type `Product` to type `Commodity`.

This has proven to be too restrictive in DRR, where Commodity Payout can operate on a basket or index. Therefore, the data type of the underlier has been updated to `Underlier` with the other benefit of making it consistent with the other payouts.

To ensure that the underlier is indeed commodity-related, conditions have been added to force the `underlier` attribute to reference a commodity-related undlier.  The `CommodityUnderlier` condition uses a switch statement to evaluate whether the underlier is an `Observable` or `Product`, and to assess it accordingly.  A new function `ObservableIsCommodity` is used to standardise this and handles the different choice types of observables and the potential recursive nature of baskets.

_Backward-incompatible changes_

The change to the data type of the `underlier` attribute is not backward-compatible.

_Review directions_

The changes can be reviewed in PR: [#3277](https://github.com/finos/common-domain-model/pull/3277) or in Rosetta.
