# *Event Model: Allocation / Split Business Events*

_What is being released_

Refactor `Allocation` and `Split` business events.

`Allocation` business event now contains two types of primitives:
- `SplitPrimitive` with the original execution split into separate executions according to the `SplitInstructions`, with the parties and quantities of each execution updated.
- `ContractFormationPrimitive` for each execution in `SplitPrimitive.after.splitTrades.execution`.

`Split` business event now only one primitive:
- `SplitPrimitive` with the original trade (e.g. execution or contract) split into separate trades according to the `SplitInstructions`, with the parties and quantities updated.

_Review Directions_

In the Textual Browser, review the functions:

- `Create_Allocation` (BusinessEvent creation function)
- `Create_Split` (BusinessEvent creation function)
- `Create_SplitPrimitive` (PrimitiveEvent creation function)
- `Create_ContractFormationPrimitive` (PrimitiveEvent creation function)
- `Qualify_Allocation` (Qualification function)
- `Qualify_Split` (Qualification function)

In the Ingestion Panel, review the samples:

- `events > allocation-multiple.xml`
- `events > allocation-single.xml`
- `events > split.xml`

