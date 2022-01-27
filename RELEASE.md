# *Event Model - Consolidation of workflow information in WorkflowStep*

_What is being released?_

This release consolidates some workflow-related attributes that were previously positioned in the BusinessEvent type into the WorkflowStep type instead.

_Details_

The following types and attributes have been modified:

- The `WorkflowStepState` type has been renamed more succinctly as `WorkflowState`.
- The `workflowEventState` attribute (now of type `WorkflowState`) has been moved from `BusinessEvent` into `WorkflowStep` and renamed simply as `workflowState`.
- The `tradeWarehouseWorkflow` attribute has been removed from `BusinessEvent`. Its underlying attributes are all included in the `workflowState` attribute that is now positioned in `WorklfowStep`:

  - `warehouseIdentity`
  - `warehouseStatus`
  - `partyCustomisedWorkflow`

- The `TradeWarehouseWorkflow` type has been deprecated as it is not used any more.

The qualification functions for `ClearingSubmission` and `ClearingRejection` have been adjusted to reflect the new structure. These two functions are not tested with sample messages as part of the CDM. In future, the qualification of Rejection / Submission shall operate at the `WorkflowStep` level rather than the `BusinessEvent`.

Similarly, the `TradeWarehousePositionNotification` qualification function was not tested and has been removed. This qualification shall also operate at the `WorkflowStep` level rather than `BusinessEvent`.

Synonyms have been adjusted to allow mapping of existing CME and DTCC messages to continue to work with the new structure. 

_Review Directions_

In the CDM Portal Textual Browser search for the types listed above to see the changes.
Use the Ingestion function to review the CME and DTCC examples.


test branch
