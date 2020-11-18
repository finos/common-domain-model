# *New ExerciseInstruction and Create_Exercise Function*

_What is being released_

A data type and function pair that allow for the representation of an instruction to exercise an option and a subsequent function that constructs the contract changes.

- `ExerciseInstruction` is a new data type that specifies the information to communicate the intention to exercise, as expressed by the party holding the rights to an option.
- `Create_Exercise` function constructs the business event that represents the exercise of an option according to the referenced `ExerciseInstruction`.

The initial structure of the data type and function are designed to support a use case of a European style Swaption that is exercised for physical settlement. The initial validation of the data type and function have been limited to that use case.
 
The current structure may be valid for other cases, but these have not yet been validated. Over time, additional use cases will be validated or used as a reference to expand the structure as needed, for example, for cash settlement, other underlying asset types and for American style options.

_Review Directions_

In the CDM portal, reviews the data type and function listed above, and load examples in the Visualisation function to see an illustration of the use of the function.
