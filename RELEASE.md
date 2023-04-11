# *Product Model - Qualification*

_Background_

This release completes the coverage of the first level of composable product qualification based on ISDA taxonomy v2.

_What is being released?_

Function updates have been made in the `cdm.product.qualification` namespace to fully support the first level of composable product qualification for all 5 asset classes: InterestRate, Credit, ForeignExchange, Equity and Commodity.

_Qualification_

- Removed `Qualify_AssetClass_InterestRate_Swap` and updated all references with the new `Qualify_AssetClass_InterestRate`
- Removed `Qualify_AssetClass_CreditDefault` and updated all references with the new `Qualify_AssetClass_Credit`
- Updated `Qualify_AssetClass_Equity` to work with `economicTerms` argument
- Renamed `Qualify_AssetClass_Equity` to `Qualify_UnderlierProduct_Equity` using `underlier` argument
- Created `Qualify_AssetClass_ForeignExchange`
- Created `Qualify_AssetClass_Commodity`

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
In the CDM Portal, select Ingestion and review the `fpml-5-10/products` and `fpml-5-12/products` samples.