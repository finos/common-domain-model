
# *Event Model - Exercise*

_Background_

This release follows the recent work on the composable business event model and the corresponding creation function `Create_BusinessEvent`.

The Exercise business event is now supported in the CDM through either a functional approach, for physical exercise of Interest Rate and Credit Swaptions only, or a declarative approach for other exercise scenarios.

_Functional Model_

For Physical exercise of Interest Rate Swaptions or Credit Default Swaptions the CDM functional model can create the required outputs through the use of a an `exercise` Instruction passed into the `Create_BusinessEvent` function.

_Declarative Model_

For Cash exercise, and for Physical exercise of Options that are not Interest Rate or Credit Default Swaptions a declarative model approach needs to be taken where the BusinessEvent is defined by a `quantityChange` Instruction to close the Option, with an associated `transfer` for Cash Exercise, or a `quantityChange` Instruction to close the Option and a separate `execution` Instruction for the replacement trade for Physical Exercise.

_What is being released?_

`ExerciseInstruction` - updated to contain the necessary inputs to functional instruct an exercise business event.
`Create_ExercisePrimitive` - new primitive event creation function used to functional create the result of the physical exercise of an Interest Rate Swaption or Credit Default Swaption.
`Create_BusinessEvent` - updated to call `Create_ExercisePrimitive` when input contains an `exercise` instruction.
`Qualify_Exercise` - updated to correctly qualify an exercise business event based on the functional or declarative model.

The CDM contains the following visualisation examples to demonstrate the approach described.
- Swaption Full Cash Exercise
- Swaption Partial Physical Exercise
- Swaption Full Physical Exercise
- Cancellable Swap Exercise

_Review Directions_

In the CDM Portal, select the Textual Browser and review the data types and functions described above

In the CDM Portal, select Instance Viewer and review the visualisation examples above in the `Exercise Business Event` folder
