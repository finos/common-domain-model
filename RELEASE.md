# *Model Optimisation: Quantity Rector - Remove Deprecated Types and Attributes*

_What is being released_

- Remove deprecated attributes `Quantity.currency` as it now defined by `ResolvablePayoutQuantity.assetIdentifier.currency` as part of the new generic quantity model.   
- Refactor type `Underlier` so it contains attribute `underlyingProduct`, and remove deprecated types `SingleUnderlier` and `Basket`.

_Review Directions_

In the Textual Browser, review types `Quantity`, `ResolvablePayoutQuantity` and `Underlier`.
