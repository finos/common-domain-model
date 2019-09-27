_What is being released_

*Reset Model Improvements - Phase 2*

This release implements the proposals to improve the Reset Model such that the Reset Primitive is consistent with other Primitive Events (i.e. that it contains both the “before” and “after” states). The before and after states are represented by the Contract State object, which is to be use to hold a history of reset values to be used in calculations which depend on multiple values. The subsequent cash flow that results from a Reset Event is now captured in a separate Cash Transfer Event. 

At this stage, the full history of Resets have not been captured in the Contract State object, however it will do so in a future iteration. 

In addition to changes to the data model, new Functions were introduced to specify how CDM users should transition a Contract through a a Reset and Payment Event. 

New Functions to specify the Reset Event: `Reset`, `NewResetPrimitive`, `ResolveUpdatedContract`, `ResolveEquityContract`. 
New Functions to specify the Cash Transfer Event: `TransferCash`, `NewCashTransferPrimitive`, `ResolveCashflow`, `EquityCashSettlementAmount`

*Contract Creation Model Improvements*

Additionally in this release, new Functions are added to specify how Executions and Contracts should be created. This reflects the previously approved proposal to split the Inception Primitive into Execution and ContractFormation Primitives and adds support for the Allocation use case.

New Functions to specify the Execution and Contract Formation: `Execute`, `NewExecutionPrimitive`, `FormContract`, `NewContractFormationPrimitive`

_Review direction_

*Equity Reset Model*

To review the above changes, we make use of the Equity Reset use case as context. In the Functions UI, the equity-swap Event Sequence now describes the steps involved in creating an Equity Swap Execution which results in an Equity Swap Contract, then a subsequent Reset and Payment Event. 

The visualisation showcases the outputs of each Function as it is executed as well as how the output of one function is used in the inputs to the next. The output JSON represent the results of example implementations of the Functions specified in the CDM and thus the JSON objects will not have satisfied all cardinality and data constraints.
