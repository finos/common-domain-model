# *Workflow Model - Intended BusinessEvent of next WorkflowStep*

_What is being released_

Modelling components for the representation of the intended business event that would form the next `workflowStep`.

_Details_

`nextWorkflowStep` has been introduced as an attribute of data type `WorkflowStep`.  This attribute allows the specification of either a `nextIntendedEvent` which allows the intended event to defined using the existing `IntentEnum`, or a `proposedInstruction`.  A `one-of` condition requires one of the attributes to be defined only.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for `WorkflowStep` and review the attributes above.
Select the Ingestion Viewer and review the sample fpml-5-10 > record-keeping > record-ex02-vanilla-swap-datadoc containing the Intent to Clear.

# *Termination Visualisations*

_What is being released?_

- STORY-458: A CDM user can visualise how Full and Partial Termination Events work when multiple Trade Lots exist