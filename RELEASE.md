# *Event Model - ContractFormationInstruction*

_What is being released?_

The ContractFormationInstruction data type has been updated to optionally take an `executionInstruction` as an input to be used in events which need to create an execution and fully formed contract in

_Details_

`ContractFormationInstruction` can now be defined using an `execution` or `executionInstructions` as input.  A `required choice` condition validates that one of the attributes is provided along with an optional `legalAgreement`.

In addition the `Create_ContractFormation` business event creation function has been adjusted to use the `executionInstruction` attribute when defined.

_Review Directions_

In the CDM Portal, select the Textual Viewer and see that the above mentioned data types have been removed.

# *Event Model - Transfer: Remove unused Transfer sub-types*

_What is being released?_

Several unused Transfer-related data types have been removed from the model

_Details_

The following data types and their corresponding synonyms have been removed:

- `CashTransferBreakdown`
- `CashTransferComponent`
- `CommodityTransferBreakdown`
- `CommodityTransferComponent`
- `SecurityTransferBreakdown`
- `SecurityTransferComponent`
- `TransferorTransferee`

_Review Directions_

In the CDM Portal, select the Textual Viewer and see that the above mentioned data types have been removed.

# *DSL Syntax - Digital Regulatory Reporting Report Definition*

_What is being released?_

This release contains DSL change to allow Digital Regulatory Reporting syntax to specify the `report` as a `type` where each attribute is associated with a `regulatory rule`, rather than a simple list of `fields` (e.g. `regulatory rule` list).

_Details_

Specifying the `report` as a `type` allows the cardinality and type to be specified and validated for each report attribute.  Furthermore, the API report output is now structured to allow transformation into other data formats (e.g. ISO 20022) possible.

_Review Directions_

Changes to report syntax will be made in an upcoming Digital Regulatory Reporting release.
