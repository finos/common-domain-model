# _Product Model_ - Security Finance trade types

_Background_

The Asset Refactoring initiative (see [#2805](https://github.com/finos/common-domain-model/issue/2805)) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes two minor adjustments following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

An optional attribute exists on the `AssetPayout` data type to uniquely identify the different types of securities financing trade types, such as a repurchase transaction or a buy/sell-back.  The name of this attribute has been updated to have a broader potential scope and not limit just to repos.  Specifically, the attribute `repoType` has been renamed to `tradeType` and its data type has been renamed from `RepoTypeEnum` to `AssetPayoutTradeTypeEnum`.  The values of the enumerator have not been changed.

The FINOS CDM documentation for the securities lending use case has been corrected; it was not correctly showing the remodelled use of AssetPayout that was implemented as a result of the Asset Refactor Taskforce.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- Rename the `repoType` to `tradeType` on `AssetPayout`.
- Rename `RepoTypeEnum` to `AssetPayoutTradeTypeEnum`.
- The two product qualification functions have been updated to use the new names:
  - `Qualify_RepurchaseAgreement`
  - `Qualify_buySellBack`.

_Review directions_

The changes can be reviewed in PR: #3267 or in Rosetta.
