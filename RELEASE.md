# *Model Optimisation: Price Refactor*

_What is being released_

Following the recent price refactor, migrate synonyms to the new model for all products including rates, equity, FX, credit and repo, and also all other event, DTCC and CME synonyms.

Remove synonyms from model attributes from `InterestRatePayout.rateSpecification`:

- `fixedRate.initialValue`
- `floatingRate.initialRate`
- `floatingRate.spreadSchedule.initialValue`
- `floatingRate.capRateSchedule.initialValue`
- `floatingRate.floorRateSchedule.initialValue`

Add synonyms to refactored `PriceNotation` attributes:

- `price.fixedInterestRate.rate`
- `price.interestRateSpread.rate`
- `price.interestRateSpread.capRate`
- `price.interestRateSpread.floorRate`
- `assetIdentifier.currency`
- `assetIdentifier.rateOption.floatingRateIndex`
- `assetIdentifier.rateOption.indexTenor`

The `RateSpecification` type on `InterestRatePayout` now has three attributes of type `FixedRateSpecification`, `FloatingRateSpecification` and `InflationRateSpecification`.  These all extend super type `RateSpecificationBase` which has an attribute `assetIdentifier` that corresponds to the `PriceNotation.assetIdentifier`.

_Review Directions_

In the Textual Browser, review the following types:

- `PriceNotation`, `Price`, `FixedInterestRate` and `InterestRateSpread`.
- `RateSpecification`, `FixedRateSpecification`, `FloatingRateSpecification` and `InflationRateSpecification`.

In the Ingestion Panel, try one of the following samples:

- `products > credit`
- `products > equity`
- `products > FX`
- `products > rates`
- `products > repo`


# *CDM Model: Equity Dividend Cash Settlement Amount Calculation*

_What is being released_

Function added to calculate the Dividend Cash Settlement Amount for Equities as defined in the SES1 legal document.

_Review Directions_

In the Textual Browser, review func `DividendCashSettlementAmount`.