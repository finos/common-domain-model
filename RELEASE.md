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

# *Event Model: Workflow Step Refactor for Clearing*

_What is being released_

The Event model in the CDM has been adjusted to cater for the clearing usecase. The concept of `WorkflowStep` has been introduced to model a step in a workflow that can either be in a state of `propose`, `accept` or `reject`.

The `WorkflowEvent` has been removed from the model and its properties have been re-organised into `WorkflowStep` and `BusinessEvent`.

`BusinessEvent` now contains `eventDate`, `effectiveDate`, `intent` and `eventQualification`.

Updated the definitions for `BusinessEvent` and `PrimitiveEvent`.

All Event Qualifications now function on the `BusinessEvent` rather then the `WorkflowEvent`.

Introduced `BusinessEventInstruction` which defines the instructions to execute a CDM function to contruct a `BusinessEvent`. 

The `BusinessEventInstruction` can be `one-of` `ClearingInstruction` or `AllocationInstructions` with the intention to add further types.

`ContractFormation` can be created _without_ a before state to allow fully formed `Contract` created without an `Execution`.

_Review Directions_

In the Textual Browser, review the following:

- types: `WorkflowStep`, `BusinessEvent`, `PrimitiveEvent`, `BusinessEventInstruction` and `ClearingInstruction`.




