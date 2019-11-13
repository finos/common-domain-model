# *Introduce the concept of Workflow Event*

_What is being released_

`WorkflowEvent` is a higher-level concept on top of `BusinessEvent` (and contains it), to be used by implementors to build their own workflow rule-book that eventually leads to a state-transition (as expressed in the `BusinessEvent`).
 - Renamed the type  `Event` to `WorkflowEvent`
 - Positioned the `BusinessEvent` type to be an attribute of the new `WorkflowEvent`
 - Renamed the `Rosetta_Workbench` synonym to `Workflow_Event`

_Review direction_

Visit the ISDA CDM Portal and navigatge to WorkflowEvent`.
