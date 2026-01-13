# *Product Model - Enhance Security Lending Qualification*

_Background_

The `Qualify_SecurityLending` function expects that a `collateralPortfolio -> collateralPosition -> product ->  TransferableProduct` exists. This is not always going to be the case.

If a trade is against cash then `collateralPortfolio -> collateralPosition -> product -> TransferableProduct` will hold the details of the cash being used as collateral - so this scenario is fine.

If a trade is against non-cash though, the collateral will be referenced using a schedule/portfolio identifier and thus there will not be a `collateralPosition` under `collateralPortfolio` at all, instead we would have a `collateralPortfolio -> portfolioIdentifer` that will hold the identifier for the collateral pool being used as collateral against this trade.

_What is being released?_

The `Qualify_SecurityLending` function has been updated to now just check for the presence of `collateral -> collateralPortfolio`.

This is generic enough to cover cash (which would be under `collateralPortfolio -> collateralPosition -> product -> TransferableProduct`) and non-cash (which would have a `collateralPortfolio -> portfolioIdentifier`).

_Review Directions_

The changes can be reviewed in PR: [#4301](https://github.com/finos/common-domain-model/pull/4301)