# *Event Model - Business Event Intent*

_What is being released_

This release normalises the representation of intent on a `BusinessEvent`, as well as on an `Instruction` passed into the `Create_BusinessEvent` function. Intent needs to be provided when a BusinessEvent cannot be uniquely qualified based on the resulting TradeState and instructions used.  Conversely where a BusinessEvent can be uniquely qualified based on the resulting TradeState and instructions then intent does not need to be provided.

- The enumeration `InstructionFunctionEnum` has been removed from the model as it duplicates the functionality of `IntentEnum`.
- The enumeration `IntentEnum` has been renamed `EventIntentEnum` and been updated to contain a simplified list only containing enumerations which are needed to uniquely qualify a `BusinessEven`t.
- BusinessEvent qualification functions have been updated to reflect the refactoring above.

_Review Directions_

In the CDM Portal, select Textual Browser and review `EventIntentEnum`.
