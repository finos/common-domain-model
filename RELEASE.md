# *Product Model - FpML mapping of Basic Price Return and Total Return Swaps*

_Background_

The `Payout` `performancePayout` was recently introduced to allow for representation of a wider variety of products, both new (Variance, Volatility, Correlation and Dividend Swaps) and already supported products (Basic Price Return and Total Return Swaps). Since the present version the `PerformancePayout` was used exclusively to represent new products, and the deprecated `EquityPayout` was still available for Basic Price and Total Return Swaps. This version removes the deprecated Equity Payout and adapts the Performance Payout to cover the two aforementioned products in all their variants.

_What is being released?_

The FpML synonyms for Basic Price and Total Return Swaps have been moved from `EquityPayout` to `PerformancePayout`.

The following types and attributes have been updated:

- `ValuationDates` type added to the model.
- `EquityValuationDates` type renamed as `PerformanceValuationDates`.
- `PerformancePriceReturnTerms` type renamed as `PriceReturnTerms`.
- `PerformanceDividendReturnTerms` type renamed as `DividendReturnTerms`.
- `DividendPeriod->dividendPeriodStartDate` attribute renamed as `startDate`.
- `DividendPeriod->dividendPeriodEndDate` attribute renamed as `endDate`.
- Enum value `TradeDate`added to `DividendDateReferenceEnum`.

The following qualification functions have been adapted for `PerformancePayout`:

- `Qualify_BaseProduct_EquitySwap`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Index`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_Index`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Basket`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_Basket`

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

fpml-5-10/products/equity
- eqd-ex01-american-call-stock-long-form.xml
- eqd-ex04-european-call-index-long-form.xml
- eqs-ex01-single-underlyer-execution-long-form.xml
- eqs-ex01-single-underlyer-execution-long-form-other-party.xml
- eqs-ex06-single-index-long-form.xml
- eqs-ex09-compounding-swap.xml
- eqs-ex10-short-form-interestLeg-driving-schedule-dates.xml
- eqs-ex11-on-european-single-stock-underlyer-short-form.xml
- eqs-ex12-on-european-index-underlyer-short-form.xml
- eqs-ex13-pan-asia-interdealer-share-swap-short-form.xml
- eqs-ex14-european-interdealer-share-swap-short-form.xml
