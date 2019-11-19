# *Addition of WorkflowEventState nad new ClearedTrade event qualification*

_What is being released_

The `isEvent ClearedTrade` definition has been update as part of the Clearing process in the CDM. It is now defined by the termination of a contract (alpha) and an execution and contract formation of the beta and gamma with a party role of `ClearingOrganisation`.

The new `WorkflowEventState` type is where the status of the clearing process will be held (i.e. Accepted, Rejected). The type `EventWorkflow` has been renamed to `WorkflowEventState` so that it is not confused with the `EventWorkflow` type. 

_Review direction_

Search for ` isEvent ClearedTrade` to see the definition of ` ClearedTrade`.
Navigate to `WorkflowEvent` and see the last attribute to see the usage of ` WorkflowEventState`. 
