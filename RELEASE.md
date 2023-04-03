# *Equity Swap - Valuation Dates Initial*

_Background_

This release updates and extends valuation dates mapping coverage for Equity Swap products.

_What is being released?_

Mapping added for `ValuationDates -> valuationDatesInitial` with FpML element `rateOfReturn -> initialPrice`.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
In the CDM Portal, select Ingestion and review the following samples:
`fpml-5-10 > incomplete-products > equity-swaps`
- eqs_ex05_single_stock_plus_fee_long_form
- eqs_ex07_long_form_with_stub `fpml-5-12 > products > equity`
- eqs_ex15_forward_starting_pre_european_interdealer_share_swap_short_form
- trs_ex04_index_ios