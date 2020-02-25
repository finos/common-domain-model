# *CDM Model: Price Model Refactor Preparation*

_What is being released_

As preparation for the upcoming model price work, refactor `Contract` and `Execution` to represent product, quantity and price consistently, which also enables a significant consolidation and simplification of synonyms.

`Contract` and `Execution` refactored to introduce type `TradableProduct` which groups together `Product`, `QuantityNotation` and `PriceNotation`, and remove the following deprecated types and attributes:

- Removed type `ExecutionPrice` and related attributes `Contract.executionPrice` and `Execution.price`.
- Removed type `ExecutionQuantity` and related attributes `Contract.contractualQuantity` and `Execution.executionQuantity`.
- Removed attributes `Contract.contractualProduct` and `Execution.product`.
- Added `Contract` data rule `ContractualProductExists`.

_Review Directions_

In the Textual Browser, review type `TradableProduct`.
