# *Product Model - Equity Qualifying Functions*

_Background_

This release extends the qualification for equity products, in particular for basic performance equity product with a single name, an index or a basket as underlier.

_What is being released?_

This release adjusts the qualification logic for equity swap products to aligned with the specified `ReturnTerms`, rather than the specified `ReturnTypeEnum`. 

- `Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Basket`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Index`

_Review Directions_

In the CDM Portal, select Textual Browser and review the corresponding qualification functions 

In the CDM Portal, select Ingestion and review the samples in the folders below havve been qualified adequately:

- fpml-5-10 > products > equity-swaps
- fpml-5-10 > incomplete-products > equity-swaps

