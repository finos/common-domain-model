# *Event Model - Mapping FpML novation events*

_Background_

Recent changes updated the FpML event mapping approach to map FpML event messages to a `WorkflowStep` instruction, i.e., a `WorkflowStep` containing a proposed `BusinessEvent`. The `WorkflowStep` instruction is then processed by a function `Create_AcceptedWorkflowStepFromInstruction` to create the corresponding fully-specified `WorkflowStep` event.

_What is being released?_

This release adds synonym mappings for FpML novation events.

_Review Directions_

In the CDM Portal, select Ingestion and review the samples below, which have been mapped to `WorkflowStep` instructions:

- fpml-5-10/processes/msg-ex52-execution-advice-trade-partial-novation-C02-00.xml
- fpml-5-10/processes/msg-ex53-execution-advice-trade-partial-novation-correction-C02-10.xml
- fpml-5-10/processes/msg-novation-from_transferor.xml

In the CDM Portal, select Instance Viewer, and review the above samples in the `FpML Processes` folder, which create fully-specified `WorkflowStep` events from the ingested instructions.
