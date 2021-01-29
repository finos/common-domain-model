# *CDM Model: Function Development - Create_Execution*

_What is Being Released_
The Create_Execution function has been updated to use an instructions object and ensure consistent design of Business Event functions in the CDM.

**Background**

Consistent design of Business Event Functions will allow for easier adoption of the CDM Event Model.  Each Business Event should create a valid CDM output object based on input instructions.  All Business Event creation functions, and Business Event qualification functions should include a description.

**Model Changes**

The `ExecutionInstruction` data type has been added to the model, containing mandatory data attributes required on an Execution Business Event.  `ExecutionDetails` data type added as an attribute within `ExecutionInstruction` to specify type and venue of Execution.  `Create_Execution` function updated to contain `ExecutionInstruction` as input object.  Descriptions on `Create_Execution` and `Qualify_Execution` updated.

*Review Directions*
In the CDM Portal, use the Textual Browser or Graphical Navigator to inspect the data types and functions referenced above.
