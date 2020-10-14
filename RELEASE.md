# *Infrastructure: Namespace reclassification*

_What is being released_

Following the latest namespace reclassification, an infrastructure change is required to enable Rosetta Core to run ingestion on CDM type `cdm.event.workflow.WorkflowStep` which was reclassified from `org.isda.cdm.WorkflowStep`.

_Review Directions_

This infrastructure change has no impact on the CDM model, nor the CDM Portal.

To run the ingestion on a `cdm.event.workflow.WorkflowStep` type in Rosetta Core, select the Ingestion panel, and run the all integration tests for synonym `CDM_Event`.
