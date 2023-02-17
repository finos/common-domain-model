# *Event Model – WorkflowStep updates*

_Background_

As part of the work done by the ISLA Trading Working Group some enhancements have been made to the WorkflowStep event model and associated functions.

_What is being released?_

This release adds a new type of WorkflowStepApproval that can be used to track whether parties included on the workflow have approved a proposedEvent or not. A new approval object of type WorkflowStepApproval has been added to the existing WorkflowStep type.

The function Create_ProposedWorkflowStep has had approval added as an input. This is the only function updated as it is the only one that deals with proposed events rather than business events.

A correction has also been made to the setting of the previousWorkflowStep in the Create_AcceptedWorkflowStepFromInstruction function.

Data Types

-	Added WorkflowStepApproval type:
     - Includes approved, party, rejectedReason and timestamp
-	Modified WorkflowStep type:
     - Added approval attribute of type WorkflowStepApproval

Functions

-	Modified Create_AcceptedWorkflowStepFromInstruction function:
- Modified previousWorkflowStep to be set to proposedWorkflowStep and not proposedWorkflowStep -> previousWorkflowStep
-	Modified Create_ProposedWorkflowStep function:
- Added approval as an input to the function which, if present, will be added into the new WorkflowStep generated as the result of the function

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect the types and functions.
