# _Product Model - Addition of Exchange attributes_

_Background_

In accordance with the CDM roadmap, the model is being expanded and updated to ensure robust support for evolving regulatory requirements. Specifically, this is for a new reporting rule called _Underlying Asset Trading Platform Identifier_. This reporting rule requires extracting the platform on which the underlying asset is traded, thus this information is given by `exchange` and `relatedExchange` attributes.

_What is being released?_

This release includes the addition of the attributes `exchange` and `relatedExchange` in the following types:
- `Commodity`
- `Security`

To achieve this, an intermediate type called `Listing` is created to prevent contamination in other types and to maintain consistency with CDM 6. The rationale behind this change is that a new field requires extracting the platform on which the underlying asset is traded which is given by `exchange` and `relatedExchange` attributes. The discussion on this matter can be found in issue [#3338](https://github.com/finos/common-domain-model/issues/3338).

_Review Directions_

In the Rosetta platform, select the Textual View and inspect each of the changes identified above.
Select the following path _cdm.base.staticdata.asset.common_ and review the expectations for the fields listed above.

Changes can be reviewed in PR: [#3519](https://github.com/finos/common-domain-model/pull/3519)

# _Product Qualification - Delineation logic for non-standard terms_

_Background_

Currently, the product qualification functions in CDM 5.x.x do not fully support qualification of Equity Products.

_What is being released?_

This release addresses this issue to accommodate Equity and Exotic Products under individual Asset Classes.

The following qualification functions are being introduced:
- `Qualify_Equity_OtherSwap` - qualifies a product as an Equity Swap (Other) where the base product qualifies as Equity Swap with non standard terms.
- `Qualify_Credit_OptionOther` - qualifies a product as a Credit Option (Other) where the base product qualifies as Credit Option with non standard terms.
- `Qualify_Commodity_OptionOther` - qualifies a product as an Commodity Option (Other) where the base product qualifies as Commodity Option with non standard terms.

The following qualification functions are being amended to avoid duplicate qualification:
- `Qualify_AssetClass_InterestRate`
- `Qualify_AssetClass_Equity`

Modifications in the existing functions include the existing clause where only `forwardPayout` is allowed. The functions will also check for a combination `forwardPayout` with `interestRatePayout` or `cashflows` to qualify the product as `AssetClass: Interest Rate` or  `AssetClass: Equity`.

_Backward-incompatible changes_

None.

_Review Directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above, navigating to file cdm > product > qualification > func and reviewing:
- the addition of three new qualification functions
- modifications to existing qualification functions

Original Issue: [#3476](https://github.com/finos/common-domain-model/issues/3476)

Changes can be reviewed in PR: [#3522](https://github.com/finos/common-domain-model/pull/3522)
