# *Business Event model - Composable Event model *

_Background_

Business events in the CDM are currently described using separate instructions and functions to create each potential business event, and those functions are not composable.  The various instructions captured in the `Instruction` data type are not structured consistently with some instructions referencing the `before` trade state, where as others do not.  This contravenes the principle of composability and nullifies the benefit of defining primitive events.

The CDM event model is therefore being refactored based on three principles:
- Develop a standardised functional logic for each primitive event, leveraging the existing functional logic developed for business events.
- Define a set of primitive instructions to be used as inputs in each primitive event function – those primitive instructions should set a consistent treatment of the before trade state.
- Rewrite the functional logic of business events by composition in terms of those primitive events’ logic.

Following the refactoring Business Events will be described in terms of the instructions used to create the events and the after TradeState(s) created.  The Primitive Event functions will describe the transformations that occur when the instructions are applied to create a business event.

_What is being released_

This release introduces core components required to develop the composable event model in the CDM. Changes are described based on the data types, enumerations and functions impacted:

_Data types_
`BusinessEvent` - new attributes `instruction`, `instructionFunction` and `after` added to describe the instructions that were used to create the event; the functional event defined by the set of instructions; and the after TradeState(s) created, respectively.
`PrimitiveInstruction` - a new data type containing the list of PrimitiveEvent instructions needed to pass into a PrimitiveEvent function
`Instruction` - new attributes `primitiveInstruction` and `before` added to describe inputs required to pass into a BusinessEvent described using the composable model.
`TermsChangeInstruction` - a new data type containing the instructions required to pass into the Terms Change primitive event function.

_Enumerations_
`InstructionFunctionEnum` - enumeration values indicating the Business Event defined by the set of PrimitiveInstructions provided.

_Functions_
`Create_BusinessEvent` - a new composable function that creates a business event from instructions containing primitive instructions and optionally a trade state.
`Create_TradeState` - a new composable function that creates a trade state from instructions containing primitive instructions and optionally a trade state.
`Qualify_ContractFormation` - qualification function updated to qualify a business event created using the composable model as contract formation.
`Qualify_Execution` - qualification function updated to qualify a business event created using the composable model as execution.
`Qualify_Partial Termination` - qualification function updated to qualify a business event created using the composable model as partial termination.
`Qualify_Termination` - qualification function updated to qualify a business event created using the composable model as termination.
`Qualify_Renegotiation` - qualification function updated to qualify a business event created using the composable model as renegotiation.
`QuantityDecreased`,`QuantityIncreased`,`QuantityDecreasedToZero` - compares quantities on a before and after `TradeState`.
`QuantityDecreasedPrimitive`,`QuantityIncreasedPrimitive`,`QuantityDecreasedToZeroPrimitive` - compares before and after quantities on a `QuantityChangePrimitive`.

_Review Directions_

