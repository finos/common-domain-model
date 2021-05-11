# *Event Model - Transfer components: Rationalised repsentation of Quantity and Observable attributes*

_What is being released?_

This release adjusts the `Transfer` type by replacing the `PriceQuantity` attribute, with separate `Quantity` and optional `Observable` attributes.  In addition, a data constraint has been added to the `Transfer` type to validate that when an `Observable` is specified that the `Quantity->unitOfAmount->financialUnit` is also specified.

_Review directions_

In the CDM Portal, select the Ingestion, and review the following samples: 

- events > compression-bilateral
- events > exercise-swaption-cash
- events > increase-contract-cash-intent
- events > increase-contract-cash-no-intent
- events > increase-xccy
- events > partial-termination-contract-cash-intent
- events > partial-termination-contract-cash-no-intent
- events > partial-termination-xccy
- events > termination-contract-with-fee
- bundles > transfer-cash-netted-bundle
- bundles > transfer-physical-exercise-bundle
- bundles > transfer-xccy-cash-netted-bundle-with-correction
