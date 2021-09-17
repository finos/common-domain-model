# *Product Model – Inflation Swaps*

_What is being released?_
Inflation Rate Indices have been added to the CDM so that Inflation Linked products can be represented.  In addition synonym mappings have been updated to support ingestion of FpML sample trades.

_Details_

- `InflationRateIndexEnum` added containing enumeration of inflation rate indices.
- Synonym mappings added to support ingestion of FpML 5.10
- The following trades containing in the `fpml-5-10 > incomplete-products > inflation` folder have been moved to `fpml-5-10 > products > inflation`
  - inflation-swap-ex01-yoy
  - inflation-swap-ex02-yoy-bond-reference
  - inflation-swap-ex03-yoy-initial-level
  - inflation-swap-ex04-yoy-interp
  - inflation-swap-ex05-zc

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant enumeration specified above.

In the CDM Portal, select the Ingestion panel and review sample trades specified above.
