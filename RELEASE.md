# *Model Optimisation: Quantity Refactor - Add Functions To Resolve Payout Quantity*

_What is being released_

Following on from the recent migration to a product-agnostic quantity model, functions have now been added to resolve the payout quantity.

- func `ResolvePayoutQuantity`: for a given `ResolvablePayoutQuantity`, the function resolves and returns the corresponding quantity from the `QuantityNotations` by matching on `AssetIdentifier`.
- func `ResolvePayout`: for a given `ContractualProduct`, the function finds all `ResolvablePayoutQuantity` instances, then for each instance, resolves the quantity, populates the `quantity` attribute on `ResolvablePayoutQuantity`, and finally returns the updated `ContractualProduct`.

Note that only the function inputs and outputs are specified natively in the CDM, with the implementation in Java.  Depending on WG feedback, further work could be undertaken to add the required syntax to support writing the implementation natively in the CDM.

_Review Directions_

- In the Textual Browser, review function specifications `ResolvePayoutQuantity` and `ResolvePayout`.
- In the downloaded CDM distributable, review corresponding Java implementations `ResolvePayoutQuantityImpl.java` and `ResolvePayoutImpl.java`.  
