# *Event Model: Add Create Workflow Step Functions*

_What is being released_

CDM functions to create a `WorkflowStep` supporting proposing a business event, accepting a business event, and rejecting a  business event. The new functions support the action of `New`, `Correct` and `Cancel` with constraints resticting the inputs to valid cases.

_Review Direction_

- New annotation type [create WorkflowStep] added similar to [create BusinessEvent].
- 4 new functions have been added with inputs: message Information, timestamp, event identifiers, parties, accounts.
  - `Create_WorkflowStep` - Business Event + action (new, correct, cancel)
  - `Create_ProposedWorkflowStep` - Proposed Instructions + action (new or correct only)
  - `Create_AcceptedWorkflowStep` - Business Event + previous proposed step reference
  - `Create_RejectedWorkflowStep` - Rejection + previous proposed step
- Action is now optional on `WorkflowStep`

# *Portal Registration Form Bugfix*

_What is being released_

For new users registering on the CDM Portal, numbers are were not permitted to be used when specifying a company name. Thsi bug has been fixed.

