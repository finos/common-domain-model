# *CDM Model: Function Development - Create_Execution*

_What is Being Released_

The Create_Execution function has been updated to use an instructions object and ensure consistent design of Business Event functions in the CDM.

**Background**

Consistent design of Business Event Functions will allow for easier adoption of the CDM Event Model.  Each Business Event should create a valid CDM output object based on input instructions.  All Business Event creation functions, and Business Event qualification functions should include a description.

**Model Changes**

The `ExecutionInstruction` data type has been added to the model, containing mandatory data attributes required on an Execution Business Event.  `ExecutionDetails` data type added as an attribute within `ExecutionInstruction` to specify type and venue of Execution.  `Create_Execution` function updated to contain `ExecutionInstruction` as input object.  Descriptions on `Create_Execution` and `Qualify_Execution` updated.

_Review Directions_

In the CDM Portal, use the Textual Browser or Graphical Navigator to inspect the data types and functions referenced above.

# *CDM Model: Primitive Harmonisation - Follow Ups*

_What is Being Released_

**Observation Event**

As part of the Primitive Harmonisation design, the creation of `Observation` data object instances should be handled independently and outside the trade life-cycle, the observation is not an event but a piece of data used in a process.  Now, `Observation` object instances will be linked to a trade (via the `TradeState` data type), which is now done via the reset process.

As such, the `ObservationPrimitive` and its associated functions are no longer needed in the model and have been removed.

Ingestion examples have also been updated to reflect this change in structure.

_Review Directions_

In the CDM Portal, use the Textual Browser or Graphical Navigator to inspect the data types and functions referenced above. The Equity Swap example within the Functions app now exemplifies how the reset process works after Primitive Harmonisation. 

**Documentation Update**

User documentation for CDM has been updated to reflect the changed brought about as part of Primitive Harmonisation. Additionally, code-snippets were checked and updated to reflect the current model syntax. Unit tests to check validity of code-snippets were also updated to catch instances where code-snippets do not match model syntax. 

_Review Directions_

In the CDM Portal, use the Documentation app to inspect the user documentation. 

**Position data type**

The `Position` data type was updated such that it now mandates a reference to `Trade`. This change accounts for the normalisation of `Contract` and `Execution` data types, which have now been consolidated into the `Trade` data type. 

_Review Directions_

In the CDM Portal, use the Documentation app to inspect the user documentation. 
