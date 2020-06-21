# *Documentation: Add descriptions to Create functions*

_What is being released_

The Functions defined in the CDM have been updated to include descriptions for their usage, inputs, outputs and conditions.

_Review Direction_

In the CDM textual browser, see the descriptions for functions:

- `Create_Execution`
- `Create_ExecutionPrimitive`
- `Create_ContractFormation`
- `Create_ContractFormationPrimitive`
- `ClearingInstruction`

# *Event Model: Add Create Workflow Step Functions*

_What is being released_

CDM functions to create a `WorkflowStep` with a business event that has happened or a `WorkflowStep` supporting proposing a business event, accepting a business event, and rejecting a business event. The new functions enable data quality remediation on a `WorkflowStep` data record with the use of the action attribute with possible values of New, Correct and Cancel and constraints restricting the inputs to valid cases.

_Review Direction_

- New annotation type [create WorkflowStep] added similar to [create BusinessEvent].
- 4 new functions have been added to create workflowStep with inputs: message Information, timestamp, event identifiers, parties, accounts.
  - `Create_WorkflowStep` - Business Event + action (new, correct, cancel)
  - `Create_ProposedWorkflowStep` - Proposed Instructions + action (new or correct only)
  - `Create_AcceptedWorkflowStep` - Business Event + previous proposed step reference
  - `Create_RejectedWorkflowStep` - Rejection + previous proposed step
- Action is now optional on `WorkflowStep`

# *Portal Registration Form Bugfix*

_What is being released_

For new users registering on the CDM Portal, numbers are were not permitted to be used when specifying a company name. This bug has been fixed.
