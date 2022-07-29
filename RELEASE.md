# *Event Model - Removal of Legacy Primitive Events*

_Background_

This release follows the recent work on the composable business event model using Instruction type and the corresponding creation function, Create_BusinessEvent.  The previous approach would composed business events with with primitive events. Theese are no longer needed. Business Events are now described through the combination of Instructions and the resulting after Trade States.

_What is being released?_

This release removes the final legacy PrimitiveEvent and adjust the associated modelling elements.
- Removed Types
  - PrimitiveEvent
  - ExecutionPrimitive
  - ContractFormationPrimitive
  - SplitPrimitive
  - QuantityChangePrimitive
  - ResetPrimitive
  - TermsChangePrimitive
  - TransferPrimitive
- Renamed functions that referred to primitive but no longer returned an event primitive type, e.g Create_ExecutionPrimitive renamed to Create_Execution
- Removed functions: QuantityDecreasedPrimitive, QuantityDecreasedToZeroPrimitive, CompareQuantityChangePrimitives, CompareQuantityChangePrimitive
- Updated all qualification functions that referred to EventPrimitive

_Review Directions_
 
In the CDM Portal, select Textual Browser and review the types and function mentioned above.
