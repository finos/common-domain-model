# *Workflow Model - Event Identifier*

_What is being released?_

This release updates the WorkflowStep creation functions to allow multiple identifiers to be associated with a single WorkflowStep.  The attribute `eventIdentifier` within data type `WorkflowStep` is of unbound cardinality, but the corresponding creation functions currently only allow a single value input.

_Review Directions_
 
In the CDM Portal, select Textual Browser and review the functions listed below:
- `Create_WorkflowStep`
- `Create_AcceptedWorkflowStep`
- `Create_ProposedWorkflowStep`
