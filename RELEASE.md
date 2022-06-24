# *Product Model - FpML mapping of Equity Swaps*

_Background_

The `PerformancePayout` was recently introduced to allow for representation of a wider variety of products, not only from the equity asset class, but also from any product involving a return determined by an observation. Recent releases introduced support for non-equity products, such as variance swaps, volatility swaps, dividend swaps, and correlation swaps.

_What is being released?_

This release updates the synonym mappings so FpML equity swaps are now mapped to the `PerformancePayout`, rather than the legacy `EquityPayout`.  

The corresponding qualification functions have been updated:

- `Qualify_BaseProduct_EquitySwap`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Index`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_Index`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Basket`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_Basket`

_Review Directions_

In the CDM Portal, select the Textual Browser and review functions above.

In the CDM Portal, select Ingestion and review samples in fpml-5-10/products/equity.
