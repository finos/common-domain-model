# *Event Model: Allocation / Split Business Events*

_What is being released_

Refactor `Allocation` and `Split` business events.

`Allocation` business event contains two types of primitives:
- `SplitPrimitive` with the original execution split into separate executions with the quantity as specified in the `AllocationInstructions`.
- `ContractFormationPrimitive` for each execution in `SplitPrimitive.after.splitTrades.execution`, updating the party based on the `AllocationInstructions`.

_Review Directions_

In the Textual Browser, review the functions:

- `Create_Allocation` (BusinessEvent creation function)
- `Create_SplitPrimitive` (PrimitiveEvent creation function)
- `Create_ContractFormationPrimitive` (PrimitiveEvent creation function)
- `Qualify_Allocation` (Qualification function)

In the Ingestion Panel, review the samples:

- `events > allocation-multiple.xml`
- `events > allocation-single.xml`
