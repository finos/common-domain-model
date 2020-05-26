# *Model Optimisation: Remove Inception Primitive*

_What is being released_

Follow up action from 2-Jul-19 Design WG minutes:
- Remove the Inception primitive and provide sample events for ExecutionPrimitive and ContractFormation. Proposal is to make use of the new ‘Function Specification’ module and start presenting events as a sequence instead of a static ingestion.

The `InceptionPrimitive` has been deprectated for some time and has now been completely removed in favour of the `ExectionPrimitive` and `ContractFormationPrimitive`. All synonymns, conditions, qualifications and function have now been updated to reflect this. Examples of Inception events provided in the CDM Portal Ingestion section have now been removed and replaced with new examples that are available in the Instance Viewer section created by executing the relevent Business Event Functions.


_Review Direction_

In the CDM Portal Review changes:

- Ingestion examples for DTCC now ingest into `ContractFormation` event.
- See CME confirm examples now qualify the beta and gamma trades as `ContractFormation`
- In the Textual browser, see the reg reporting definition of `NewTrade` now reports on Execution or Contract Formation (but not both to avoid double reporting)
- In the Textual browser, see updated qualifications: `Qualify_Novation`, `Qualify_PartialNovation`, `Qualify_TradeWarehousePositionNotification`
- In the Instance Viewer, see new examples for FX Forward, CDS and Repo in the Execution and Contract Formation Business section
- In the Textual browser, see that `PostInceptionState` has been renamed to `PostContractFormationState` for consistency
