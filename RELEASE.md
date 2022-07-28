# *Product Model - Equity Qualifying Functions*

_What is being released?_

This release contains adjustments to the qualification logic for equity swap products:

- `Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Basket`
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Index`

The logic has been changed to determine qualification based on the specified `ReturnTerms`, rather than the specified `ReturnTypeEnum`. 

_Review Directions_

In the CDM Portal, select Ingestion and review the samples in the folders below:

- fpml-5-10 > products > equity-swaps
- fpml-5-10 > incomplete-products > equity-swaps

