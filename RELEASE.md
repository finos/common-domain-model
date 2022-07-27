# *Event Model - Removal of Unused Synonym Mappings for the Legacy Event Model and Corresponding Ingestion Samples*

_Background_

This release follows the recent work on the composable business event model using Instructions and the corresponding creation function, `Create_BusinessEvent`. The previous approach using business events composed with primitive events was illustrated with several mocked up samples. Those were enabled with artificial XML schema and associated synonym mapping. They are no longer needed.

_What is being released?_

This release removes the legacy event model XML schema (based on primitive event), the corresponding synonym mappings (i.e., synonym source `Workflow_Event`), and  ingestion samples previously illustrating the business event composed with primitive events.

_Review Directions_

In the CDM Portal, select Textual Browser and review that the `Workflow_Event` synonym mappings have been removed.

In the CDM Portal, select Ingestion and review that the legacy event samples have been removed.
