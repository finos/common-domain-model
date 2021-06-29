# Event Model - Contribution of ISLA - CDM for Securities Lending

_What is being released?_

This release contributes modelling components proposed by ISLA to extend the CDM event model in term of Securities Lending products. The release extends existing CDM event model concepts to support allocation, re-allocation, initial Settlement, and part and full return, of a Securities Lending contract.  In addition a new product and event model is introduced for the billing function.  Visualisation examples are included for all events covered as part of the contribution in a new folder `Security Lending`.

Changes to existing data types and qualification functions have been made where appropriate to accomodate the additions to the model.

_Details_

_Allocation and Re-Allocation_

The `Create_Allocation` and `Create_SplitPrimitive` functions have been extended to be fully formed functions that are able to support multiple allocations and the specification of an ancillary party.  A new `Create_Reallocation` function has been introduced to allow specification of a reallocation event through the `ReallocationInstruction` data type consisting of: the original block trade, existing splits to be decreased, existing splits to be increased, and details of any new splits produced by the reallocation event. 

Updates to Existing Data Types, Enumerations and Functions

- `AllocationBreakdown` - updated `partyReference` attribute to `counterparty` to allow the new party to be specified, and which side of the trade they are being allocated into. Cardinality of `quantity` updated to allow specification of all quantities associated with the individual split.
- `SplitPrimitive` - additional condition added to ensure the quantities of the resulting split trades total that of the block trade.
- `PartyRoleEnum` - Role of `AgentLender` added to the enumeration list
- `IntentEnum` - Intent of `Reallocation` added to the enumeration list
- `Create_SplitPrimitive` - assigns a `closedState` and a `positionStatus` of `Allocated`
- `Create_QuantityChangePrimitive` - updated input to allow specification of a list of quantities on an input `TradeState` that require changing.

New Data Types and Functions

- `ReallocationInstruction`
- `DecreasedTrade`
- `IncreasedTrade`
- `Create_Reallocation`
- `Qualify_Reallocation`

Visualisation

- See examples `Allocation` and `Reallocation` 

_New, Part and Full Return Settlement_

The `Create_Transfer` function has been extended to support creation of cash and security transfers representing the settlement of a security lending transaction.  A new `Create_Return` function has been introduced to allow specification of the part or full return of a Security Lending Transaction through the `ReturnInstruction` data type consisting of: the trade being partially or fully returned, the quantity being returned, and the return date.

Updates to Existing Data Types and Functions

- `TransferInstruction` - new attribute `payerReceiver` and `quantity` added to specify values when part of a return event.
- `Create_Transfer` - allow specification of an event date.
- `Create_TransferPrimitive` - creates cash and security movements for a Security Finance transaction and sets the `PositionStatusEnum` to Settled when Transfers have been applied to a Security Finance transaction.
- `Create_CashTransfer` - creates cash movements for a Security Finance transaction, assigns `settlementOrigin` of the payout used to create the cash transfer.
- `Qualify_CashAndSecurityTransfer` - updated function to qualify based only on transfers associated with the event date.

New Data Types and Functions

- `ReturnInstruction`
- `Create_Return`
- `Create_SecurityFinanceTransfer`
- `Qualify_FullReturn`
- `SecurityFinanceCashSettlementAmount`

Visualisation

- The workflow of the new, partial, and full return settlement events are demonstrated using the CDM workflow event model reflecting the instruction and settlement of each event.
- See examples `New Settlement Workflow`, `Part Return Settlement Workflow` and `Full Return Settlement Workflow`.

_Billing_

The process of invoicing fees associated with a Security Lending transaction has been modelled with the addition of the `SecurityLendingInvoice` data type, to represent the information needing to be passed from one party to the other.  The `Create_SecurityLendingInvoice` function has been introduced to allow specification of the information required to populate an invoice, including calculation of the billing amounts, through the `BillingInstruction` data type consisting of: the sending and receiving party, the billing start and end date, a `BillingRecordInstruction`, and a `BillingSummaryInstruction`.

Updates to Existing Data Types and Functions

- `FloatingAmount` and `FixedAmount` - updated to allow specification of `CalculationPeriodData` when the period for the calculation is not specified parametrically on the payout (e.g. billing for a partial period of a security lending transaction).  `calculationPeriod` is now aliased as part of each function, either as the specified `CalculationPeriodData` or the regular period on the `interestRatePayout`

- DayCountFraction - all Day Count Fraction functions have been simplified to use alias defined in `FloatingAmount` or `FixedAmount`.

New Data Types, Enumerations and Functions

- `SecurityLendingInvoice`
- `BillingInstruction`
- `BillingRecord`
- `BillingSummary`
- `BillingRecordInstruction`
- `BillingSummaryInstruction`
- `RecordAmountTypeEnum`
- `Create_SecurityLendingInvoice`
- `Create_BillingRecords`
- `Create_BillingRecord`
- `Create_BillingSummary`
- `Create_SecurityFinanceTradeStateWithObservations`
- `Create_SecurityFinanceReset`
- `ResolveSecurityFinanceBillingAmount`


Visualisation
- See example `Billing`.

_Legal Agreement Model_

Updates to Existing Enumerations

- `LegalAgreementEnum` - `GMSLA` added to enumeration list.
- `LegalAgreementPublisherEnum` - `BNYM`, `ISLA`, and `JPMorgan` added to enumeration list.

_Utility Functions_

A number of utility functions have been updated or added to the model in order to facilitate implementation of the above model components.

New Data Types and Functions

- `Create_TradableProduct` - creates a tradable product object from a set of given inputs.
- `ConvertToAdjustableOrAdjustedOrRelativeDate` - Utility function to convert from AdjustableOrAdjustedOrRelativeDate to AdjustableOrAdjustedOrRelativeDate.
- `FilterQuantityByFinancialUnit` - Filter list of quantities based on unit type.
- `UpdateAmountForEachQuantity` - Updates all quantities on each price quantity with the new amount.
- `UpdateAmountForEachMatchingQuantity` - Updates any quantity from the list of new quantities if the unit of amount matches.
- `DeductAmountForEachMatchingQuantity` - Deducts the amount for any quantity from the list of new quantities if the unit of amount matches.
- `Create_Quantity` - Create Quantity with given amount and unit of amount.
- `Create_UnitType` - Create UnitType with given currency or financial unit.
- `ExtractCounterpartyByRole` - Extracts from a list of Counterparty data types, the Counterparty that corresponds to the role i.e. Party1 or Party2.
- `ReplaceParty` - Removes the old party, and adds the new party.
- `Create_PayerReceiver` - Create PayerReceiver with given payer and receiver.
- `ExtractFixedLeg` - Filters a list of InterestRatePayouts to extract a Fixed Leg.
- `CalculationPeriodRange` - Outputs CalculationPeriodData for a given start date, end date and business days.
- `Create_TradableProduct` - Creates a new TradableProduct with the given inputs.
- `Create_SplitTrades` - Loops through each trade and calls Create_SplitTrade.
- `Create_SplitTrade` - Creates a split trade from a block trade and a breakdown.


Updates to Existing Data Types and Functions

- `ResolveCashflow` - creates cash movements for a Security Finance transaction.
- `Create_TerminationQuantityChangePrimitive` - sets a `closedState` and `positionStatus` on the Terminated trade
- `CloseTrade` - renamed `TerminateContract` and updated to set a `closedState` and `positionStatus`.

_Review directions_

In the CDM Portal, select the Textual Browser and search for any of the changes specified above.
In Rosetta, select Visualisation and search for any of the examples defined above.
