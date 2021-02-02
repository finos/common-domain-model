# *CDM Model: Function Development - Create_Execution*

_What is Being Released_

The Create_Execution function has been updated to use an instructions object and ensure consistent design of Business Event functions in the CDM.

**Background**

Consistent design of Business Event Functions will allow for easier adoption of the CDM Event Model.  Each Business Event should create a valid CDM output object based on input instructions.  All Business Event creation functions, and Business Event qualification functions should include a description.

**Model Changes**

The `ExecutionInstruction` data type has been added to the model, containing mandatory data attributes required on an Execution Business Event.  `ExecutionDetails` data type added as an attribute within `ExecutionInstruction` to specify type and venue of Execution.  `Create_Execution` function updated to contain `ExecutionInstruction` as input object.  Descriptions on `Create_Execution` and `Qualify_Execution` updated.

_Review Directions_

In the CDM Portal, use the Textual Browser or Graphical Navigator to inspect the data types and functions referenced above.

# *CDM Model: Primitive Harmonisation - Remove Observation Event*

_What is Being Released_

As part of the Primitive Harmonisation design, the Observation event should be removed as these events no longer relate specificaly to a Trade. Instead `Observation` instances can be created independently from other data types and is decoupled from the Trade event model.

To reflect this, the `ObservationPrimitive` data type and its associated functions have been removed from the model. Ingestion examples have also been updated to reflect this change in structure. 

_Review Directions_

In the CDM Portal, use the Textual Browser or Graphical Navigator to inspect the data types and functions referenced above. The Equity Swap example within the Functions app now exemplifies how the reset process works after Primitive Harmonisation. 

# *CDM Model: Primitive Harmonisation - Documentation Update*

_What is Being Released_

User documentation for CDM has been updated to reflect the changed brought about as part of Primitive Harmonisation. Additionally, code-snippets were checked and updated to reflect the current model syntax. Unit tests to check validity of code-snippets were also updated to catch instances where code-snippets do not match model syntax. 

_Review Directions_

In the CDM Portal, use the Documentation app to inspect the user documentation. 



