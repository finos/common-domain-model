## Java test scenarios improvement - Java ingestion examples added to CDM Examples module

_Background_

While ingestion examples are available in CDM 7, equivalent Java-based ingestion examples were not previously included in CDM 6, limiting users’ ability to understand and implement ingestion workflows in earlier versions.

_What is being released?_

This release extends the CDM Examples module by adding the Java ingestion examples.

These examples demonstrate how to ingest an **FpML 5.13 confirmation** and transform it into CDM objects (TradeState and WorkflowStep).

The ingestion process is implemented using the following functions:

- `Ingest_FpmlConfirmationToWorkflowStep`
- `Ingest_FpmlConfirmationToTradeState`

Both examples use the XML configuration file to define the mapping between the FpML input and CDM structures.

These additions focus on extending the examples module only. No changes have been made to the underlying ingestion logic or CDM model.

_Review Directions_

Changes can be reviewed in PR: [#4588](https://github.com/finos/common-domain-model/pull/4588)

