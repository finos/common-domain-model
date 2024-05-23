# _Product Model - Principal Amount Conditions_

_Background_

The condition that require the amount (or its present value) to be populated on a principal payment is too strict. There are cases where this amount is not known in advance because the quantity of the payout may evolve, e.g. in scenarios such as "Mark-to-Market" Cross-Currency Swaps or resettable Equity Swaps.

_What is being released?_

This release relaxes the condition on `PrincipalPayment` so that the amount (or its present value) are optional. Instead, to preserve sufficient data quality constraints on the model, new conditions are implemented at the Payout level to enforce the presence of those attributes only in the relevant scenarios.

_Data types:_

- `PrincipalPayment`:
  - Relaxed the `PrincipalAmount` constraint to be `optional choice` instead of `required choice` between the `principalAmount` and `presentValuePrincipalAmount` attributes.
- `PrincipalPaymentSchedule`:
  - Added a new `InitialPrincipalAmountExists` condition that requires `initialPrincipalPayment`, when it exists, to have either its `principalAmount` or `presentValuePrincipalAmount` attributes populated. 
- `PayoutBase`:
  - Added a new `FinalPrincipalAmountExists` condition that requires `finalPrincipalPayment`, when it exists and the quantity is not resettable, to have either its `principalAmount` or `presentValuePrincipalAmount` attributes populated. 

_Review directions_

In the Rosetta Platform, select the Textual View and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2933
