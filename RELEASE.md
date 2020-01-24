# *Model Optimisation: Price Refactor for Equity Swaps*

_What is being released_

Following the recent price refactor, the synonyms, data rules and functions for Equity Swaps have been migrated to the new product-agnostic, generic price model.

Remove synonyms from deprecated model attributes:

- `Contract.contractualProduct.economicTerms.payout.equityPayout.priceReturnTerms.initialPrice`
- `Execution.product.contractualProduct.economicTerms.payout.equityPayout.priceReturnTerms.initialPrice`

Add synonyms to refactored model attributes:

- `Contract.contractualPrice.priceNotation.price.*`
- `Execution.price.priceNotation.price.*`

Remove deprecated attribute `PriceReturnTerms.initialPrice`.

Add data rule that checks triangulation between `Price`, `Quantity` (no. of units) and `Quantity` (notional) that applies to any applicable `Contract` or `Execution` (not just Equity Swaps).

_Review Directions_

In the Textual Browser, review the following:

- types: `PriceReturnTerms`, `PriceNotation`
- data rules: `Contract_cashPrice_quantity_noOfUnits_triangulation`, `Execution_cashPrice_quantity_noOfUnits_triangulation`
- functions: `CashPriceQuantityNoOfUnitsTriangulation`
 
In the Ingestion Panel, try one of the following samples:
- `products > equity > eqs-ex01-single-underlyer-execution-long-form.xml`
- `products > equity > eqs-ex10-short-form-interestLeg-driving-schedule-dates.xml`
