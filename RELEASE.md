# *Product - Modifying and adding qualification functions*

_Background_

Qualification functions are used extensively in CDM and Digital Regulatory Reporting to determine the type of product.

Several functions were added and modified in CDM 5 without the changes being applied to CDM 6 & 7. This update adds qualification functions to CDM 6 & 7

_What is being released?_

Added qualification functions

- `Qualify_TotalReturnSwap_Index` - This function qualifies a product as a Total Return Swap (Index) where the base product qualifies as Credit Swap and the index underlier for performance leg qualifies as Credit.
- `Qualify_Equity_OtherForward` - This function qualifies a product as a Equity Forward (Other) where the base product qualifies as Equity Forward with non-standard terms.

Modified qualification functions

- `Qualify_AssetClass_Credit` - Updated to check for credit underlier of the performance payout.
- `Qualify_BaseProduct_EquityForward` - Updated to check that `nonStandardisedTerms` on the settlement payout are either absent or False

_Review Directions_

Changes can be reviewed in PR: [#4176](https://github.com/finos/common-domain-model/pull/4176)
