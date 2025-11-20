# *CDM Ingestion - FpML Confirmation Ingestion Tests*

_Background_

The ingestion framework has been extended to provide clearer, tested examples of how FpML confirmation documents can be transformed into CDM objects.  
This update introduces new tests validating the full ingestion workflow using the Rosetta XML ObjectMapper configured for FpML 5.13 confirmations.  
The new tests ensure that FpML documents are correctly parsed, validated, and ingested into `TradeState` and `WorkflowStep` objects.

_What is being released?_

This release adds the following ingestion components and scenarios:

Ingestion base class

- **`AbstractIngestionTest`**  
  Provides a shared foundation for XML ingestion tests, including:
  - Initialising a Rosetta XML ObjectMapper using the FpML confirmation XML configuration (`fpml-5-13`)
  - Setting the expected XML schema location
  - Injecting the ingestion functions:
    - `Ingest_FpmlConfirmationToTradeState`
    - `Ingest_FpmlConfirmationToWorkflowStep`
  - Utility method for loading and configuring the XML mapper (`getXmlMapper`)

Ingestion scenarios

- **`IngestFpmlConfirmationTest`**  
  Adds full ingestion scenarios demonstrating how to convert FpML confirmation samples into CDM objects:
  - **FpML Confirmation to TradeState**  
    Validates ingestion of a vanilla interest rate swap FpML confirmation, ensuring that the XML mapper configuration is correctly applied and that a valid `TradeState` instance is produced.
  - **FpML Confirmation to WorkflowStep**  
    Validates ingestion of an execution advice document for a partial novation, producing a valid `WorkflowStep` instance and confirming end-to-end ingestion workflow integrity.

These scenarios provide practical examples for users integrating FpML confirmation ingestion pipelines with CDM.

_Backward-incompatible changes_

None.

_Review Directions_

Changes can be reviewed in: [PR#4196](https://github.com/finos/common-domain-model/pull/4196)
