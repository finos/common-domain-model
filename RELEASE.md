# *Workflow Model - Creation of Workflow Steps with multiple Event Identifiers*

_What is being released?_

This release adjusts the WorkflowStep creation functions to allow multiple identifiers to be associated with a single WorkflowStep.  The attribute `eventIdentifier` within data type `WorkflowStep` is of unbound cardinality.  The corresponding creation functions should accordingly permit multiple values as an input for that attribute.

_Review Directions_
 
In the CDM Portal, select Textual Browser and review the functions listed below:
- `Create_WorkflowStep`
- `Create_AcceptedWorkflowStep`
- `Create_ProposedWorkflowStep`
