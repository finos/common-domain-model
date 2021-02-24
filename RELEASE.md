# *CDM Model: Event Model - ContractFormationPrimitive*

_What is Being Released_

Bug Fix - The `ContractFormationPrimitive` event has been updated to remove a redundant condition and correct commentary for the remaining condition to be applied to the event object.

**Model Changes**
The conditions on`ContractFormationPrimitive` have been rationalised to one condition which continues to ensure that trade details are persisted when a contract is formed from an input trade (i.e. _Before_ contains `TradeState`) from an execution object (or otherwise).

_Review Directions_

In the CDM Portal, use the Textual Browser to inspect the data type `ContractFormationPrimitive'.

