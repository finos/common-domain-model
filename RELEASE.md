# *Model Optimisation: Quantity Refactor*

_What is being released_

Clean up tasks following the recent model quantity refactor:

- Rename attribute `NonNegativeQuantitySchedule.quantity` to `NonNegativeQuantitySchedule.initialQuantity`.
- Add FpML synonym mappings logic to populate `NonNegativeQuantitySchedule.initialQuantity` only when a quantity schedule exists.
- Add attribute `ResolvablePayoutQuantity.resolvedQuantity` to be populated when the quantity is resolved (based on the aseet identifier).

_Review Directions_

In the Textual Browser, review types `NonNegativeQuantitySchedule` and `ResolvablePayoutQuantity`. 
