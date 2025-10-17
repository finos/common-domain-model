## Ingest - Mapping for WorkflowStep, EquityOptions, and CDS

*Background*

Ingest functions for FpML Confirmation to CDM are available in the CDM 7-dev version for beta testing by the CDM community. There are still gaps between the original synonym mapping and ingest functional mapping which will be addressed in this PR.

*What is being released?*

Mapping has been completed for the following areas in CDM:

- Workflow Step
    - Expanded `PrimitiveInstruction` mapping function for different message types (`ConfirmationAgreed`, `ExecutionNotification`, `RequestClearing` `RequestConfirmation`, `ClearingConfirmed`, `ExecutionAdvice`, `ExecutionAdviceRetracted`, `ExecutionNotification`, `RequestClearing`, `TradeChangeAdvice`)
    - Created new `MapPrimitiveInstruction` along with function with new functions for amendments, terminations, and event based model mapping (`MapAmendmentToPrimitiveInstruction`, `MapTerminationToPrimitiveInstruction`, `MapTradingEventsBaseModelToPrimitiveInstruction`)
    - Extended workflow step mapping for `EventTimestamps` and `MessageInformation`
    

- Vanilla Equity Option
    - Further mapping for Equity Bermuda `multipleExercise`
    - Created a new functions `MapBasketConstituentQuantity` and `MapEquityBaseFinancialUnit`
    - Added mapping for a multiplier on non negative quantity schedules
    - Mapped `passThrough` and `averagingFeature` on an option payout feature attribute

- Broker Equity Option
    - Adding `MapEquityPremiumListToTransferStateList` function for `BrokerEquityOption` product type
    - Further mapping for payout fields (`unit type`, `price`)

- Credit Default Swaps
    - Mapped `protectionTerms` fields (`gracePeriodExtension`, `obligationAcceleration`, `repudiationMoratorium`, `multipleHolderObligation`, `multipleCreditEventNotices`)
    - Mapped physical and cash settlement terms in the `MapCreditDefaultSwapChoiceToSettlementTerms` function

- Common
    - Added coverage for `TradeAmendmentContent` in `GetFpmlTrade` function
    - Added functions `MapFeeTypeEnumWithScheme` and `MapMessageAction` 
 
- DateTime
    - Added 2 new functions `MapFpmlDateTimeListToDateTimeList` and `MapEventTimestampQualification`

- PriceQuantity
    - Add multiplier mapping to `MapCurrencyAmountToQuantity` function

- Other
    - Added new values to `MapFeeTypeEnum` function

  

*Review Directions*

Changes can be reviewed in PR: [#4091](https://github.com/finos/common-domain-model/pull/4091)
