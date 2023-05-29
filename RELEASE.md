# _Payout underlier for Equity Option Baskets_

_Background_

An issue was identified with the underlier mapping from FpML to CDM for Equity Option Basket products. The mapping from CDM was not generating an underlier and its corresponding baskets for these products and therefore some samples were not accurately qualified.

This release addresses this mapping issue, correctly generating the underlier and its corresponding baskets.

_What is being released?_

_Translate_

Added minor changes for mapping coverage of equity options with baskets.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

fpml-5-10/incomplete-products/equity-options

- eqd ex08 basket long form
- eqd ex20 nested basket
- eqd ex21 flat weight basket
- eqd ex26 mixed asset basket
