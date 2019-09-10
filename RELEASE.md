_What is being released_

*Proposed model changes as part of an on-going effort to augment the product coverage with securities*

1) `Affirmation` specifies the information required when affirming a trade.
2) Add post-trade processing states  `Confirmed` and `Affirmed` to `WorkflowStatusEnum`.
3) During trade post-processing, confirmation and affirmation can occur in any order.  Change `EventWorkflow` to store all `workflowStatus` so all previous states can be checked.  E.g. it can be checked whether `workflowStatus` contains both `Confirmed` and `Affirmed`.
4) The `TransferSettlementEnum` (e.g. `DeliveryVsPayment` etc) is known at execution time, therefore it can be specified as part of the `SettlementTerms`.
5) Proposals for `Settle` and `NewTransferPrimitive` funcs, initially supporting the use case of security settlement via `DeliveryVsPayment` method.

_Review direction_

In the CDM Textual Browser:
- Review classes `Affirmation`, `EventWorkflow` and `WorkflowStatusEnum`, `SettlementTerms`.
- Review functions `Settle` and `NewTransferPrimitive`.
