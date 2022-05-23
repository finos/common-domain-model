# *Event Model - Mapping to CDM representation of business event*

_Background_

The current mapping of FpML event messages to CDM requires pointing to `BusinessEvent` with a set of `PrimitiveEvent`.  This approach deviates from the recommended design that uses a set of after `TradeState` and a set of `Instruction`, each including a before `Tradestate`. Structurally, many FpML events messages are  analogous to the latter. A few model changes have been made and synonyms have been adjusted to enable a more systematic mapping to a `WorkflowStep` instruction. The latter can then be processed by a CDM function to create the corresponding fully-specified `WorkflowStep` event.  

_What is being released?_

This release focuses on the synonym mapping and infrastructure changes to map FpML events to `WorkflowStep` instruction and subsequently invokes a function to create a `WorkflowStep` event.

_Model changes_

* Add new type `EventInstruction` to group the attributes required for `BusinessEvent` creation, i.e., List of `Instruction`, an optional `EventIntentEnum` and an event date. 
* Update `WorflowStep` attributes `proposedEvent` and `nextEvent` to use the type `EventInstruction`.
* Add new function `Create_AcceptedWorkflowStepFromInstruction` to create a fully-specified `WorkflowStep` event from an input `WorkflowStep` instruction.

_Ingestion and infrastructure changes_

- The `FpML_Processes` samples for Contract Formation and Termination events have been mapped to `WorkflowStep` instruction.
- The changes in `synonym-cdm-fpml-processes.rosetta` are focused on types `WorkflowStep`, `EventInstruction` and `Instruction`.
- The `WorkflowStep` instructions are then processed by the `Create_AcceptedWorkflowStepFromInstruction` function to create a `WorkflowStep` event.

_Review Directions_

In the CDM Portal, select the Textual Browser and review types and functions mentioned above.

In the CDM Portal, select Ingestion and review the samples below, which have been mapped to `WorkflowStep` instructions:

- fpml-5-10/processes/msg-ex51-execution-advice-trade-initiation-C01-00.xml
- fpml-5-10/processes/msg-ex58-execution-advice-trade-initiation-F01-00.xml
- fpml-5-10/processes/msg-ex63-execution-advice-trade-initiation.xml
- fpml-5-10/processes/msg-partial-termination.xml

In the CDM Portal, select Instance Viewer, and review the samples in the `FpML Processes` folder, which create `WorkflowStep` events from the ingested instructions.
