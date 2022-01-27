# *Event Model - Consolidation of workflow information in WorkflowStep*

_What is being released?_

This release consolidates some workflow-related attributes that were previously positioned in the BusinessEvent type into the WorkflowStep type instead.

_Details_

The following types and attributes have been modified:

- The `WorkflowStepState` type has been renamed more succinctly as `WorkflowState`.
- The `workflowEventState` attribute (now of type `WorkflowState`) has been moved from `BusinessEvent` into `WorkflowStep` and renamed simply as `workflowState`.
- The `tradeWarehouseWorkflow` attribute has been removed from `BusinessEvent`. Its underlying attributes are all included in the `workflowState` attribute that is now positioned in `WorklfowStep`:

  - `warehouseIdentity`
  - `warehouseStatus`
  - `partyCustomisedWorkflow`

- The `TradeWarehouseWorkflow` type has been deprecated as it is not used any more.

The qualification functions for `ClearingSubmission` and `ClearingRejection` have been adjusted to reflect the new structure. These two functions are not tested with sample messages as part of the CDM. In future, the qualification of Rejection / Submission shall operate at the `WorkflowStep` level rather than the `BusinessEvent`.

Similarly, the `TradeWarehousePositionNotification` qualification function was not tested and has been removed. This qualification shall also operate at the `WorkflowStep` level rather than `BusinessEvent`.

Synonyms have been adjusted to allow mapping of existing CME and DTCC messages to continue to work with the new structure. 

_Review Directions_

In the CDM Portal Textual Browser search for the types listed above to see the changes.
Use the Ingestion function to review the CME and DTCC examples.

# *Product Model - Equity option strike price FpML mapping*

_What is being released?_

This release fixes the FpML mapping of strike price for equity options.

_Details_

The FpML synonym mappings for equity option strike price `Price->perUnitOfAmount` attribute is updated based on the underlier.

- For an equity underlier the `perUnitOfAmount` is mapped to `FinancialUnitEnum->Share`.
- For an index underlier the `perUnitOfAmount` is mapped to `FinancialUnitEnum->IndexUnit`.

_Review Directions_

In the CDM Portal, select Ingestion and review the updated samples in the fpml-5-10 > products > equity folder:

- eqd-ex01-american-call-stock-long-form
- eqd-ex04-european-call-index-long-form

# *Infrastructure - Validation of inherited types*

_What is being released?_

This release contains a bug fix related to validation of data types that inherit from other data types.

 - E.g. the `type` definition is specified using the `extends` keyword: `type InterestRatePayout extends PayoutBase`.
 
Previously, any super type validation failures, such as cardinality or condition failures, were not included in the validation diagnostics.

- E.g. when validating an `InterestRatePayout`, validation failures from `PayoutBase` were ignored.

_Review Directions_

In the CDM Portal, select Ingestion and review the Validation panel for any sample. 

# *Product Model - Commodity total notional quantity FpML mapping*

_What is being released?_

This release adds FpML mapping of total notional quantity for commodity products.

_Details_

The FpML synonym mappings for `totalNotionalQuantity` have been added to `PriceQuantity->quantity` for both `CommodityPayout` and `FixedForwardPayout`.

_Review Directions_

In the CDM Portal, select Ingestion and review all samples in the fpml-5-10 > products > commodity folder.
