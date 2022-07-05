# *Product Model - Equity Payout Removal*

_Background_

The new `Payout` `PerformancePayout` was recently introduced to allow for representation of a wider variety of products, both new (Variance, Volatility, Correlation and Dividend Swaps) and already supported ones (Basic Price Return and Total Return Swaps).  This release removes the deprecated `EquityPayout` and adapts all related functions to use the `PerformancePayout`.

_What is being released?_

- Removed `EquityPayout` and associated deprecated types.
- Functions associated to `EquityPayout` have been removed or adapted.

_Data types_

The following types have been adapted to `PerformancePayout`:

- `Payout` - removed `equityPayout` attribute
- `SettlementOrigin` - removed `equityPayout` attribute
- `EquitySwapMasterConfirmation2018` - updated condition
- `Create_SecurityFinanceReset` - updated description

The following types have been removed from the model:

- `EquityPayout`
- `EquityDividendReturnTerms`
- `EquityPriceReturnTerms`
- `EquityValuation`

_Enumerations_

- Value `TradeDate` added to `DividendDateReferenceEnum`
- Value `Variance`, `Volatility` and `Dividend` removed from `ReturnTypeEnum`

_Functions_

The following functions have been added to the model:

- `ResolvePerformanceObservationIdentifiers`
- `ResolveEquityValuationTime`
- `ResolvePerformanceReset`
- `NewSingleNamePerformancePayout`
- `ResolvePerformancePeriodStartPrice`

The following functions have been removed from the model:

- `ResolveEquityObservationIdentifiers`
- `ResolveEquityValuationDate`
- `ResolveEquityValuationTime`
- `ResolveEquityReset`
- `NewSingleNameEquityPayout`
- `ResolveEquityPeriodStartPrice`
- `ResolveEquityPeriodEndPrice`

The following functions have been adapted to `PerformancePayout`:

- `ResolvePerformanceReset`
- `EquityCashSettlementAmount`
- `EquityPerformance`
- `Create_ResetPrimitive`
- `CalculateTransfer`
- `ResolveTransfer`
- `NewEquitySwapProduct`

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
