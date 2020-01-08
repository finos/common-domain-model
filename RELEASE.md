# *Model Optimisation: Quantity Refactor - Add Functions To Resolve Payout Quantity*

_What is being released_

Following on from the recent migration to a product-agnostic quantity model, functions have now been added to resolve the payout quantity.

- func `ResolvePayoutQuantity`: for a given `ResolvablePayoutQuantity`, the function resolves and returns the corresponding quantity.
- func `ResolvePayout`: for a given `ContractualProduct`, the function finds all `ResolvablePayoutQuantity` instances, resolves the quantity, populates the attribute `ResolvablePayoutQuantity.quantitySchedule.quantity`, and returns the updated `ContractualProduct`.

_Review Directions_

In the Textual Browser, review func `ResolvePayoutQuantity` and `ResolvePayout`, and corresponding Java implementations `ResolvePayoutQuantityImpl.java` and `ResolvePayoutImpl.java`.  
