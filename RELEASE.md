# *CDM Model: Function Development - Create_ContractFormation*

_What is Being Released_

The `Create_ContractFormation` function has been updated to use an instructions object and ensure consistent design of Business Event functions in the CDM.

**Background**
Consistent design of Business Event Functions will allow for easier adoption of the CDM Event Model.  Each Business Event should create a valid CDM output object based on input instructions.  All Business Event creation functions, and Business Event qualification functions should include a description.

**Model Changes**
The `ContractFormationInstruction` data type has been added to the model, containing mandatory data attributes required on a Contract Formation Business Event.  `Create_ContractFormation` function updated to contain `ContractFormationInstruction` as input object.  Descriptions on `Create_ContractFormation` and `Qualify_ContractFormation` updated.

_Review Directions_

In the CDM Portal, use the Textual Browser or Graphical Navigator to inspect the data types and functions referenced above.
