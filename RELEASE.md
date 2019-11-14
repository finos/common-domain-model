# *Introduce the concept of Workflow Event*

_What is being released_

`WorkflowEvent` is a higher-level concept on top of `BusinessEvent` (and contains it), to be used by implementors to build their own workflow rule-book that eventually leads to a state-transition (as expressed in the `BusinessEvent`).
 - Renamed the type  `Event` to `WorkflowEvent`
 - Positioned the `BusinessEvent` type to be an attribute of the new `WorkflowEvent`
 - Renamed the `Rosetta_Workbench` synonym to `Workflow_Event`
 
 `Event Sequence Sample Testing` event sequence sample files are located in the [CDM](https://github.com/REGnosys/rosetta-cdm/tree/master/src/main/resources/cdm-sample-files/event-sequences) codebse and used to generate visualisations in CDM Portal. These files will now be automatically tested to ensure that they are valid sueqences at the CDM Portal build time. This will ensure that they will allways be compatible with any model changes that are made.

_Review direction_

Visit the ISDA CDM Portal and navigatge to WorkflowEvent`.
