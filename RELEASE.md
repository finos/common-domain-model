# *Product Model - Support for Uncollateralised trades*

_Background_

In certain scenarios it is possible for securities lending trades to be set up with no collateral defined i.e. where there is an agreement between the borrower and the lender that there will be no cash or non-cash collateral posted as collateral against the trade.

This is commonly seen between internal counterparties, i.e. intra-group trades.

_What is being released?_

A new option is being added to the CollateralTypeEnum of "Uncollateralised".

This allows trades to specifically state that there is no collateral expected to be posted against them.

_Review directions_

The changes can be reviewed in PR: [#4300](https://github.com/finos/common-domain-model/pull/4300)