# *Equity Qualifying Functions Release Notes*

_Background_

This release extends the qualification for equity products, in particular for basic performance equity options with an index or a basket as underlier.

_What is being released?_

* Two new qualifying functions: `Qualify_EquityOption_PriceReturnBasicPerformance_Index` and `Qualify_EquityOption_PriceReturnBasicPerformance_Basket`.
* The fixing of the ISDA_Taxonomy synonym values for index transaction types. The current value is `Index` but it has to be `SingleIndex`.

_Functions_

*product-common-func*
Added the `Qualify_EquityOption_PriceReturnBasicPerformance_Index` and `Qualify_EquityOption_PriceReturnBasicPerformance_Basket` functions since they were missing.