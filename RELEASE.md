# Product Model - Support for Uncollateralised trades
_Background_

In certain scenarios it is possible for securities lending trades to be set up with no collateral defined i.e. where there is an agreement between the borrower and the lender that there will be no cash or non-cash collateral posted as collateral against the trade. This is commonly seen between internal counterparties, i.e. intra-group trades.

_What is being released?_

AssetPayoutTradeTypeEnum has been updated to add a new option of "SecurityLending"
Qualify_SecurityLending has been updated to first check for tradeType being set to "SecurityLending".
If this is set then the trade is immediately qualified as a securities lending trade, which allows uncollateralised securities lending trades to be qualified.
If this is not set then the collateral is checked for trades that are collateralised with non-cash or cash

_Review Directions_

Changes can be reviewed in PR: [#4522](https://github.com/finos/common-domain-model/pull/4522)
