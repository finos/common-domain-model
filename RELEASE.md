## Ingest - Mapping for WorkflowStep, EquityOptions, and CDS

*Background*

Ingest functions for FpML Confirmation to CDM are available in the CDM 7-dev version for beta testing by the CDM community. There are still gaps between the original synonym mapping and ingest functional mapping which will be addressed in this PR.

*What is being released?*

Mapping has been completed for the following areas in CDM:

- Workflow Step
    - `PrimitiveInstruction` for different message types
    - Event timestamps
    - Message information
    - Event actions

- Vanilla Equity Option
    - Further mapping for Equity Bermuda `multipleExercise`
    - Created a new function `MapBasketConstituentQuantity` for basket constituent quantity
    - Added mapping for a multiplier on non negative quantity schedules
    - Mapped `passThrough` and `averagingFeature` on an option payout feature

- Broker Equity Option
    - Adding `MapEquityPremiumListToTransferStateList` function for `BrokerEquityOption` product type
    - Further mapping for payout fields (`unit type`, `price`)

- Credit Default Swaps
    - Mapped `protectionTerms` fields (`gracePeriodExtension`, `obligationAcceleration`, `repudiationMoratorium`, `multipleHolderObligation`, `multipleCreditEventNotices`)
    - Mapped physical and cash settlement terms

*Review Directions*

Changes can be reviewed in PR: #4091
