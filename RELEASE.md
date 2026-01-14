# *Product Model - Security-Lending Qualification Updates*

_Background_

The `Qualify_SecurityLending` function expects that a `collateralPortfolio -> collateralPosition -> product -> TransferableProduct` exists. This is not always going to be the case.

If a trade is against cash then `collateralPortfolio -> collateralPosition -> product -> TransferableProduct` will hold the details of the cash being used as collateral.

However, if a trade is against non-cash, the collateral will be referenced using a schedule/portfolio identifier and thus there will not be a collateralPosition under collateralPortfolio, but rather a `collateralPortfolio -> portfolioIdentifer` that will hold the identifier for the collateral pool being used as collateral against this trade.

_What is being released?_

The `Qualify_SecurityLending` function has been updated to just check for the presence of `collateral -> collateralPortfolio` which is generic enough to cover cash and non-cash.

_Review directions_

The changes can be reviewed in PR: [#4336](https://github.com/finos/common-domain-model/pull/4336)
